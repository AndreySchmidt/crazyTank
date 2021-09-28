package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Map {
    private TextureRegion texture;
    private TextureRegion textureWall;
    private TextureRegion textureWallList[][];

    private int hpWall = 2;
    // размер карты в штуках текстуры травы сколько клеток 1280 / 40 and 720 / 40
    public static final int SIZE_X = 1280;
    public static final int SIZE_Y = 720;

    public static final int SIZE_CELL = 20;
    public static final int SIZE_CELL_X = 64;
    public static final int SIZE_CELL_Y = 36;
//    public static final int SIZE_CELL = 40; трава 40 px стена 20 px поэтому такая запутка

    private int obstackles[][];

    public Map(TextureAtlas atlas) {
//        this.textureWallList = atlas.findRegion("walls");
        this.texture = atlas.findRegion("grass40");
        this.textureWall = atlas.findRegion("block");
        this.obstackles = new int[SIZE_X][SIZE_Y];

        this.makeWallArray();
    }

    private void makeWallArray() {
//        for(int i = 0; i < SIZE_CELL_X; i++) {
//            for(int j = 0; j < 3; j++) {
//                this.obstackles[i][SIZE_CELL_Y - 1 - j] = this.hpWall;
//            }
//        }
        for(int i = 0; i < SIZE_CELL_X; i++) {
            for(int j = 0; j < SIZE_CELL_Y; j++) {
                int cx = (int) (i / 4);
                int cy = (int) (j / 4);

                if(cx % 2 == 0 && cy % 2 == 0)
                {
                    this.obstackles[i][j] = this.hpWall;
                }

            }
        }
    }

    public boolean checkWallBulletCollision(Bullet bullet) {
//        в какой клетке находится пуля
        int cx = (int) (bullet.getPosition().x / SIZE_CELL);
        int cy = (int) (bullet.getPosition().y / SIZE_CELL);

        // пуля в пределах нашей карты
        if(cx >=0 && cy >= 0 && cx <= SIZE_CELL_X && cy <= SIZE_CELL_Y)
        {
            if(this.obstackles[cx][cy] > 0)
            {
                this.obstackles[cx][cy] -= bullet.getDamage();
                bullet.deactivate();
                return true;
            }
        }

        return false;
    }

    public void render(SpriteBatch batch) {
        // заполнение всего поля травой
        for(int i = 0; i < SIZE_X / 40; i++){
            for(int j = 0; j < SIZE_Y / 40; j++) {
                batch.draw(this.texture, i * 40, j * 40);
            }
        }

        // заполним стены
        for(int i = 0; i < SIZE_CELL_X; i++){
            for(int j = 0; j < SIZE_CELL_Y; j++) {
                if(this.obstackles[i][j] > 0) {
                    batch.draw(this.textureWall, i * SIZE_CELL, j * SIZE_CELL);
                }
            }
        }
    }

    // проверка могу ли я поставить обьект в точку на карте
    public boolean isAreaEmpty(float x, float y, float halfSize) {
        int leftX = (int) ((x - halfSize) / SIZE_CELL);
        int rightX = (int) ((x + halfSize) / SIZE_CELL);
        int topY = (int) ((y + halfSize) / SIZE_CELL);
        int bottomY = (int) ((y - halfSize) / SIZE_CELL);

        if(leftX < 0)
        {
            leftX = 0;
        }
        if(rightX > SIZE_CELL_X)
        {
            rightX = SIZE_CELL_X - 1;
        }

        if(bottomY < 0)
        {
            bottomY = 0;
        }
        if(topY >= SIZE_CELL_Y)
        {
            topY = SIZE_CELL_Y - 1;
        }

        for(int i = leftX; i <= rightX; i++)
        {
            for (int j = bottomY; j <= topY; j++)
            {
                if(this.obstackles[i][j] > 0)
                {
                    return false;
                }
            }
        }
        return true;
    }
}
