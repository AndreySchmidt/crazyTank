package com.mygdx.game.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Direction;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Utils;
import com.mygdx.game.Weapon;

public abstract class Tank {
    protected MyGdxGame game;
    protected TextureRegion texture;
    protected TextureRegion textureHp;
    protected Vector2 position;
    protected Vector2 tmp; // вредно создавать объекты во время игры для производительности, буду всаливать сюда всякое

    protected float speed;
    protected float angle;

    protected float angleTurret;
    protected float fireTimer;

    protected int height;
    protected int width;

    protected Weapon weapon;

    protected int hp;
    protected int hpMax; // без модификатора паблик, протектед, привейт будет виден только внутри пакета (каталог units)

    protected Circle circle;

    public Tank (MyGdxGame game) {
        this.game = game;
        this.tmp = new Vector2 (0.0f, 0.0f);
    }

    public void render (SpriteBatch batch) {
        batch.draw(this.texture, position.x - this.width / 2, position.y - this.height / 2, this.width / 2, this.height / 2, this.width, this.height, 1, 1, this.angle);
        batch.draw(this.weapon.getTexture(), position.x - this.width / 2, position.y - this.height / 2, this.width / 2, this.height / 2, this.width, this.height, 1, 1, this.angleTurret);

        if(this.hp < this.hpMax) {
            batch.setColor(0, 0 , 0, 0.5f);
            batch.draw(this.textureHp, this.position.x - this.width / 2 -1, this.position.y + this.height /2 + 4 -1, (float) 44, 10);
            batch.setColor(1, 0 , 0, 0.5f);
            batch.draw(this.textureHp, this.position.x - this.width / 2, this.position.y + this.height /2 + 4, (float) this.hp / this.hpMax * 40, 8);
            batch.setColor(1, 1 , 1, 1);
        }
    }

//    public abstract void update(float dt) ;

    public void update(float dt) {
        this.fireTimer += dt;
        if(this.position.x < 0.0f) {
            this.position.x = 0.0f;
        }else if(this.position.x > Gdx.graphics.getWidth()) {
            this.position.x = Gdx.graphics.getWidth();
        }

        if(this.position.y < 0.0f) {
            this.position.y = 0.0f;
        }else if(this.position.y > Gdx.graphics.getHeight()) {
            this.position.y = Gdx.graphics.getHeight();
        }

        this.circle.setPosition(this.position);
//        this.circle.setPosition(this.position.x / 2, this.position.y /2);
    }

    public Vector2 getPosition() {
        return this.position;
    }

    public Circle getCircle() {
        return this.circle;
    }

    public void takeDamage(int damage) {
        this.hp -= damage;
        if(this.hp <= 0) {
            this.destroy();
        }
    }

    protected void fire () {
        if(this.fireTimer >= this.weapon.getFirePeriod()) {
            this.fireTimer = 0.0f;
//            float projectileSpeed = 320.0f;
            float angleRadian = (float) Math.toRadians(this.angleTurret);
            this.game.getBulletEmitter().activate(this, this.position.x, this.position.y, this.weapon.getProjectileSpeed() * (float) Math.cos(angleRadian), this.weapon.getProjectileSpeed() *(float) Math.sin(angleRadian), this.weapon.getDamage(), this.weapon.getProjectileLifeTime());
        }
    }

    public abstract void destroy();

    public void rotateTurretToPoint (float pointX, float pointY, float dt) {
        float angleTo = Utils.getAngle(position.x, position.y, pointX, pointY);
        angleTurret = Utils.makeRotation(angleTurret, angleTo, 180.0f, dt);
        angleTurret = Utils.angleToFromNegPiToPosPi(angleTurret);
    }

    public void move(Direction direction, float dt){
        this.tmp.set(this.position);
        this.tmp.add(this.speed * direction.getVx() * dt, this.speed * direction.getVy() * dt);
        if(this.game.getMap().isAreaEmpty(this.tmp.x, this.tmp.y, this.width / 2))
        {
            this.angle = direction.getAngle();
            this.position.set(this.tmp);
        }

//        this.angle = direction.getAngle();
//        this.position.add(this.speed * direction.getVx() * dt, this.speed * direction.getVy() * dt);
    }
}