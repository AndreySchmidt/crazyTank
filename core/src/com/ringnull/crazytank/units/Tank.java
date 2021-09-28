package com.ringnull.crazytank.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.ringnull.crazytank.GameScreen;
import com.ringnull.crazytank.utils.Direction;
import com.ringnull.crazytank.utils.TankOwner;
import com.ringnull.crazytank.utils.Utils;
import com.ringnull.crazytank.Weapon;
import com.ringnull.crazytank.crazyTank;

public abstract class Tank {

    public GameScreen game;
    public TextureRegion texture;
    public TextureRegion textureHp;

    public Weapon weapon;

    public Vector2 position;
    // для промежуточных временных расчетов создадим tmp вектор
    public Vector2 tmp;

    public float speed; // пиксель за секунду
    public float angle = 0.0f;
    public float angleTurret = 0.0f;
    public float fireTimer;

    public int width;
    public int height;

    // линия здоровья
    public int hp;
    public int hpMax;

    // для фиксации попадания пули окружность вокруг танка
    protected Circle circle;

    public TankOwner ownerType;

    public Tank (GameScreen game) {
        this.game = game;
        this.tmp = new Vector2(0.0f, 0.0f);
    }

    public TankOwner getOwnerType() {
        return this.ownerType;
    }

    public Circle getCircle() {
        return this.circle;
    }

    // получение урона танком
    public void takeDamage(int damage){
        this.hp -= damage;
        if(this.hp <= 0){
            this.destroy();
        }
    }

    // уничтожение танка
    public abstract void destroy();

    public void render (SpriteBatch batch) {
        batch.draw(this.texture, this.position.x - this.width / 2, this.position.y - this.height / 2, this.width / 2, this.height / 2, this.width, this.height, 1, 1, this.angle);
        batch.draw(this.weapon.getTexture(), this.position.x - this.width / 2, this.position.y - this.height / 2, this.width / 2, this.height / 2, this.width, this.height, 1, 1, this.angleTurret);

        // рисуем полоску жизни только, если не 100 процентов hp
        if(this.hp < this.hpMax){
            batch.setColor(0,0,0,0.5f); // черный полупрозрачный (перемножение с цветом картинки)
            batch.draw(this.textureHp, this.position.x - this.width / 2 - 2, this.position.y + this.height / 2 - 2, 44, 12);
            batch.setColor(1,0,0,0.5f); // красный полупрозрачный (перемножение с цветом картинки)
            batch.draw(this.textureHp, this.position.x - this.width / 2, this.position.y + this.height / 2, ((float) this.hp / this.hpMax) * 40, 8);
            batch.setColor(1,1,1,1); // белый (возвращаем цвет по умолчанию)
        }
    }

//    public void render (SpriteBatch batch) {
//        batch.draw(this.texture, this.position.x - this.width / 2, this.position.y - this.height / 2, this.width / 2, this.height / 2, this.width, this.height, 1, 1, this.angle, 0, 0, this.width, this.height, false, false);
//        batch.draw(this.weapon.getTexture(), this.position.x - this.width / 2, this.position.y - this.height / 2, this.width / 2, this.height / 2, this.width, this.height, 1, 1, this.angleTurret, 0, 0, this.width, this.height, false, false);
//    }

    public void update (float dt){
        this.fireTimer += dt;
        if(this.position.x < 0.0f){
            this.position.x = 0.0f;
        }
        if(this.position.x > Gdx.graphics.getWidth()){
            this.position.x = Gdx.graphics.getWidth();
        }
        if(this.position.y < 0.0f){
            this.position.y = 0.0f;
        }
        if(this.position.y > Gdx.graphics.getHeight()){
            this.position.y = Gdx.graphics.getHeight();
        }

        // перемещаем окружность для попадания пули в танк за картинкой танка
        circle.setPosition(this.position);
    }
//    public abstract void update (float dt);

    // pointX, pointY координаты мышки
    public void rotateTurret(float pointX, float pointY, float dt){
        // какой угол между турелью и мышкой (на входе 2 вектора)
        float angleTo = Utils.getAngle(this.position.x,this.position.y, pointX, pointY);

        // повернуть башню со скоростью 180.0
        this.angleTurret = Utils.makeRotation(this.angleTurret, angleTo, 180.0f, dt);
        // проверка башня в пределах углов pi
        this.angleTurret = Utils.angleToFromNegPiToPosPi(this.angleTurret);
    }

    public Vector2 getPosition() {
        return this.position;
    }

    // движение в направлении с проверкой по карте
    public void move(Direction direction, float dt){
        // могу ли я двигаться в какую-то точку, сохраню во временный вектор значение текущей позиции танка
        this.tmp.set(this.position);

        // прибавлю к временному вектору новый вектор куда едем для проверки можно ли туда двигаться
        // двигаем (this.direction.getVx() будет 0,1,-1 и тд, от этого зависит направление движения)
        this.tmp.add(this.speed * direction.getVx() * dt, this.speed * direction.getVy() * dt);
        // проверяю по карте можно ли туда ехать
        if(this.game.getMap().isAreaClear(this.tmp.x, this.tmp.y, this.width)){
            this.angle = direction.getAngle();
            // установить значение вектора равным tmp вектору
            this.position.set(this.tmp);
        }
    }

    public void fire () {
        if(this.fireTimer >= this.weapon.getFirePeriod()){
            this.fireTimer = 0.0f;

            float angleRadian = (float) Math.toRadians(this.angleTurret);

            // умножить скорость на направление по x и y
            float vx = this.weapon.getProjectileSpeed() * (float) Math.cos(angleRadian);
            float vy = this.weapon.getProjectileSpeed() * (float) Math.sin(angleRadian);

            this.game.getBulletEmitter().activate(this, this.position.x, this.position.y, vx, vy, this.weapon.getDamage(), this.weapon.getProjectileLifeTime());
        }
    }
}
