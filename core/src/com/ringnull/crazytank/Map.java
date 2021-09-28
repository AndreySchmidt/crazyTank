package com.ringnull.crazytank;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class Map {

    public enum WallType {
        // типы твердая, мягкая и нерушимая стена NONE нет стены NONE(0, 0, false); -- нет разницы что писать в ней
        HARD(0, 3, true, false, false),
        SOFT(1, 2, true, false, false),
        INDESTRUCTIBLE(2, 1, false, false, false),
        WATER(3, 1, false, false, true),
        NONE(0, 0, false, true, true);
        // индекс кубика сверху вниз в текстуре (картинка powerUps)
        int index;
        // единицы мощности стены
        int maxHp;
        // рушимая или нет стена
        boolean destructible;

        // может ли через препятствие проходить юнит
        boolean isUnitPassable;
        // может ли через препятствие пролетать пуля
        boolean isProjectilePassable;

        // конструктор enum
        WallType(int index, int maxHp, boolean destructible, boolean isUnitPassable, boolean isProjectilePassable){
            this.index = index;
            this.maxHp = maxHp;
            this.destructible = destructible;
            this.isProjectilePassable = isProjectilePassable;
            this.isUnitPassable = isUnitPassable;
        }
    }
    // клетка
    private class Cell {
        public WallType type;
        public int hp;

        public Cell(WallType type) {
            this.type = type;
            this.hp = type.maxHp;
        }

        // урон клетке
        public void damage(){
            if(this.type.destructible){
                this.hp--;
                if(this.hp <= 0){
                    this.type = WallType.NONE;
                }
            }
        }

        public void changeType(WallType type){
            this.type = type;
            this.hp = type.maxHp;
        }
    }

    // размер игрового пространства
    public static final int SIZE_BATCH_X = Gdx.graphics.getWidth();
    public static final int SIZE_BATCH_Y = Gdx.graphics.getHeight();

    // сколько кубиков по 40 пикселей на окне
    public static final int SIZE_X = 32;
    public static final int SIZE_Y = 18;

    // сколько кубиков по 20 пикселей на окне умножение на 2 потому, что блок стены меньше в два раза, чем трава (она 40)
    public static final int SIZE_SMALL_X = SIZE_X * 2;
    public static final int SIZE_SMALL_Y = SIZE_Y * 2;

    public static final int CELL_SIZE = 40;
    public static final int CELL_SMALL_SIZE = 20;

    private TextureRegion grassTexture;
    private TextureRegion wallTexture;

    private TextureRegion[][] wall2Texture;
    private Cell cells[][];
    // массив препятствия
    private int obstacles[][];

    public Map(TextureAtlas atlas){

        this.grassTexture = atlas.findRegion("grass40");
        this.wallTexture = atlas.findRegion("block");
        // временная картинка .split(CELL_SMALL_SIZE, CELL_SMALL_SIZE)
        // .split(CELL_SMALL_SIZE, CELL_SMALL_SIZE) разрезать найденный регион на куски по 20 пикселей
        this.wall2Texture = new TextureRegion(atlas.findRegion("obstacles")).split(CELL_SMALL_SIZE, CELL_SMALL_SIZE);
//        this.wall2Texture = new TextureRegion(atlas.findRegion("walls")).split(CELL_SMALL_SIZE, CELL_SMALL_SIZE);

        // препятствия из расчета квадратика в 20 пикселей
        this.obstacles = new int[SIZE_SMALL_X][SIZE_SMALL_Y];
        // координатная сетка начинается слева снизу
        // this.obstacles[1][1] = 5; (5 это вес стены или препятствия для разрушения)
        // рисуем стену (переделать потом)
//        for(int x = 0; x < SIZE_SMALL_X; x++){
//            for(int y = 0; y < 3; y++){
//                this.obstacles[x][SIZE_SMALL_Y - y - 1] = 5;
//            }
//        }

//        // рисуем стену
//        for(int x = 0; x < SIZE_SMALL_X; x++){
//            for(int y = 0; y < SIZE_SMALL_Y; y++){
//                int cx = x / 4;
//                int cy = y / 4;
//
//                // если четные значения x и y
//                if(cx % 2 == 0 && cy % 2 == 0){
//                    // ставим стену
//                    this.obstacles[x][y] = 5;
//                }
//            }
//        }

        // рисуем стену 2
        this.cells = new Cell[SIZE_SMALL_X][SIZE_SMALL_Y];
        for(int x = 0; x < SIZE_SMALL_X; x++){
            for(int y = 0; y < SIZE_SMALL_Y; y++){

                this.cells[x][y] = new Cell(WallType.NONE);
                int cx = x / 4;
                int cy = y / 4;

                // если четные значения x и y
                if(cx % 2 == 0 && cy % 2 == 0){
                    // ставим стену случайно сгенерим блоки
                    if(MathUtils.random() < 0.7f){
                        this.cells[x][y].changeType(WallType.HARD);
                    } else {
                        this.cells[x][y].changeType(WallType.SOFT);
                    }
                }
            }
        }
        // по краям карты поставить непробиваемые стены
        for(int i = 0; i < SIZE_SMALL_X; i++){
            // низ
            this.cells[i][0].changeType(WallType.INDESTRUCTIBLE);
            // верх
            this.cells[i][SIZE_SMALL_Y - 1].changeType(WallType.INDESTRUCTIBLE);
        }
        for(int i = 0; i < SIZE_SMALL_Y; i++){
            // низ
            this.cells[0][i].changeType(WallType.INDESTRUCTIBLE);
            // верх
            this.cells[SIZE_SMALL_X - 1][i].changeType(WallType.INDESTRUCTIBLE);
        }
    }

    public void checkWallCollision(Bullet bullet){
        // в какой клетке находится пуля
        int cx = (int) bullet.getPosition().x / CELL_SMALL_SIZE;
        int cy = (int) bullet.getPosition().y / CELL_SMALL_SIZE;

        // чтобы не получить иксепшн эррэйИндексАутофБаунд, убедимся, что пуля в пределах экрана
        if(cx >= 0 && cy >= 0 && cx < SIZE_SMALL_X && cy <= SIZE_SMALL_Y){
            // это проверка попаданий в препятствия obstacles на данный момент он пустой, ибо препятствия перенесены в ячейки
            if(this.obstacles[cx][cy] > 0){
                // вычесть из препятствия урон пули
                this.obstacles[cx][cy] -= bullet.getDamage();
                bullet.deactivate();
            }


//            if(this.cells[cx][cy].type != WallType.NONE){
            // это проверка попаданий в препятствия cells (если непроходим для снарядов, то)
            if(!this.cells[cx][cy].type.isProjectilePassable){
                // вычесть из препятствия урон пули
                this.cells[cx][cy].damage();
                // деактивируй пулю
                bullet.deactivate();
            }
        }
    }

    // проверка возможности перемещения в координаты, может там есть препятствия
    // координаты и размер объекта
    public boolean isAreaClear(float x, float y, float size){
        // пол размера нашего объекта
        float halfSize = size / 2;
        //найдем левую правую и верхнюю нижнюю границу в клетках
        // левая граница объекта упирается сюда
        int leftX = (int) (x - halfSize) / CELL_SMALL_SIZE;
        // правая
        int rightX = (int) (x + halfSize) / CELL_SMALL_SIZE;

        int bottomY = (int) (y - halfSize) / CELL_SMALL_SIZE;
        int topY = (int) (y - halfSize) / CELL_SMALL_SIZE;

        // чтобы при проверке границы не вылетел иксепшн
        if(leftX < 0){
            leftX = 0;// ограничение объекта где он будет стоять
        }
        if(rightX >= SIZE_SMALL_X){
            rightX = SIZE_SMALL_X - 1;// ограничение объекта где он будет стоять
        }
        if(bottomY < 0){
            bottomY = 0;// ограничение объекта где он будет стоять
        }
        if(topY >= SIZE_SMALL_Y){
            topY = SIZE_SMALL_Y - 1;// ограничение объекта где он будет стоять
        }

        // это препятствия из массива obstacles (2 массива препятствий, еще cells)
        for (int i = leftX; i <= rightX; i++){
            for (int j = bottomY; j <= topY; j++){
                // если есть препятствие, то двигаться туда нельзя
                if(this.obstacles[i][j] > 0){
                    return false;
                }
            }
        }

        // это препятствия из массива cells
        for (int i = leftX; i <= rightX; i++){
            for (int j = bottomY; j <= topY; j++){
                // если есть препятствие, то двигаться туда нельзя
//                if(this.cells[i][j].type != WallType.NONE){
                // тип местности непроходим для юнита
                if(!this.cells[i][j].type.isUnitPassable){
                    return false;
                }
            }
        }

        return true;
    }

    public void render(SpriteBatch batch){
        // рисуем траву
        for (int i = 0; i < SIZE_X; i++){
            for (int j = 0; j < SIZE_Y; j++){
                batch.draw(this.grassTexture, i * CELL_SIZE, j * CELL_SIZE);
            }
        }

        // рисуем блоки стены
        for (int i = 0; i < SIZE_SMALL_X; i++){
            for (int j = 0; j < SIZE_SMALL_Y; j++){
                // смотрим в массив препятствий, если есть, то рисуем стену (в дальнейшем переделать), разные препятствия
                if(this.obstacles[i][j] > 0){
                    batch.draw(this.wallTexture, i * CELL_SMALL_SIZE, j * CELL_SMALL_SIZE);
                }
            }
        }

        // рисуем блоки стены 2
        for (int i = 0; i < SIZE_SMALL_X; i++){
            for (int j = 0; j < SIZE_SMALL_Y; j++){
                // смотрим в массив препятствий, если есть, то рисуем стену (в дальнейшем переделать), разные препятствия
                if(this.cells[i][j].type != WallType.NONE){
                    // левый индекс типа ячейки какая картинка по высоте cells[i][j].type.index, правая количество жизни cells[i][j].hp - 1
                    batch.draw(this.wall2Texture[this.cells[i][j].type.index][this.cells[i][j].hp - 1], i * CELL_SMALL_SIZE, j * CELL_SMALL_SIZE);
//                    batch.draw(this.wall2Texture[this.cells[i][j].type.index][this.cells[i][j].hp - 1], i * CELL_SMALL_SIZE, j * CELL_SMALL_SIZE);
                }
            }
        }
    }
}
