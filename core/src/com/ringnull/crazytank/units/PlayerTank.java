package com.ringnull.crazytank.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.ringnull.crazytank.GameScreen;
import com.ringnull.crazytank.ScreenManager;
import com.ringnull.crazytank.Weapon;
import com.ringnull.crazytank.crazyTank;
import com.ringnull.crazytank.utils.Direction;
import com.ringnull.crazytank.utils.TankOwner;

public class PlayerTank extends Tank{

    // корректно работать со строками через StringBuilder для вывода строки счета и жизней
    private StringBuilder tmpString;
    private int lives;
    private int score;

    public PlayerTank(GameScreen gameScreen, TextureAtlas atlas) {
        super(gameScreen);
        this.tmpString = new StringBuilder();
        this.texture = atlas.findRegion("playerTankBase");
        this.textureHp = atlas.findRegion("bar");
//        this.texture = new Texture("tank.png");
        this.weapon = new Weapon(atlas);

        this.position = new Vector2(100.0f, 100.0f);
        this.speed = 100;

        this.width = this.texture.getRegionWidth();
        this.height = this.texture.getRegionHeight();

        this.hpMax = 10;
        this.hp = hpMax;
        this.circle = new Circle(this.position.x, this.position.y, (this.width + this.height) / 2);
        this.lives = 5;
        this.ownerType = TankOwner.PLAYER;
    }

    public void addScore(int amount){
        this.score += amount;
    }

    // при уничтожении танка уменьшить количество жизней на единицу и восстановить здоровье
    @Override
    public void destroy() {
        this.lives--;
        this.hp = this.hpMax;
    }

    public void checkMovement (float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            this.move(Direction.LEFT, dt);
//            this.position.x -= this.speed * dt;
//            this.angle = 180.0f;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            this.move(Direction.RIGHT, dt);
//            this.position.x += this.speed * dt;
//            this.angle = 0.0f;
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            this.move(Direction.UP, dt);
//            this.position.y += this.speed * dt;
//            this.angle = 90.0f;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            this.move(Direction.DOWN, dt);
//            this.position.y -= this.speed * dt;
//            this.angle = 270.0f;
        }
    }

    public void renderScore(SpriteBatch batch, BitmapFont font){
        // отрисовать счет в точке x 10 y 700
//        font.draw(batch, "Score: " + this.score + "\nLives: " + this.lives, 10, 700);
        // Каждый раз при вызове метода renderScore обнуляем строку
        this.tmpString.setLength(0);
        // формируем строку
        this.tmpString.append("Score: ").append(this.score).append("\nLives: ").append(this.lives);
        // отрисовать счет в точке x 10 y 700
        font.draw(batch, this.tmpString, 10, 700);
    }

    public void update (float dt) {
        this.checkMovement(dt);

        // куда смотрит мышь (она в другой системе координат, стандартной, ноль на верху по y, поэтому его надо перевернуть)
//        float mx = Gdx.input.getX(); // x совпадает (ось таже, что и в libGdx, слева направо)
//        float my = Gdx.graphics.getHeight() - Gdx.input.getY(); // из-за разницы координатной сетки вычитаем из высоты окна
//        float my = Gdx.input.getY(); // так как используется вьюпорт, то координаты пишу такие же как в батч то есть ось у снизу вверх как в батче

        // при изменении размеров окна и масштаба надо делать пересчет положения мыши из-за ресайза окна
        // сначала сделать вектор2 с оконными координатами мыши (использую временный вектор)
//        this.tmp.set(mx, my);
        // отдать координаты мыши на экране, он возвращает координаты мыши в нашем мире (батч и окно не одно и то же) из-за ресайза окна
        // Вьюпорт преобразуй координаты мыши относительно окна в координаты относительно игрового мира
//        ScreenManager.getInstance().getViewPort().unproject(this.tmp);
//        this.tmp = ScreenManager.getInstance().getViewPort().unproject(this.tmp);// можно не приравнивать видимо this.tmp = он по ссылке изменит вектор переданный

//        this.rotateTurret(mx, my, dt);
        // теперь используем не координаты мыши в окне, а координаты мыши в игровом мире (нужно при ресайзе окна и изменении масштаба батча)
//        this.rotateTurret(this.tmp.x, this.tmp.y, dt);

        this.rotateTurret(game.getMousePosition().x, game.getMousePosition().y, dt);

        // единожды нажат спейс if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
        //единожды нажата кнопка мыши в окне Gdx.input.justTouched()

        // если жмем на кнопку мыши
        if(Gdx.input.isTouched()) {
            this.fire();
        }

        super.update(dt);
    }
}
