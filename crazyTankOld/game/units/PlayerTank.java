package com.mygdx.game.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Direction;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Utils;
import com.mygdx.game.Weapon;

public class PlayerTank extends Tank {
    private int lifeQuan = 5;

    public PlayerTank(MyGdxGame game, TextureAtlas atlas) {
        super(game);
        this.texture = atlas.findRegion("playerTankBase");
        this.textureHp = atlas.findRegion("bar");
        this.position = new Vector2(100.0f, 100.0f);
        this.speed = 100.0f;
        this.height = this.texture.getRegionHeight();
        this.width = this.texture.getRegionWidth();
        this.weapon = new Weapon(atlas);
        this.hpMax = 10;
        this.hp = this.hpMax;
        this.circle = new Circle(this.position.x, this.position.y, (this.width + this.height) / 2);
    }

    public void update (float dt) {
//        this.fireTimer += dt;
        this.checkMovement(dt);
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

        float angleTo = Utils.getAngle(position.x, position.y, mouseX, mouseY);
        angleTurret = Utils.makeRotation(angleTurret, angleTo, 180.0f, dt);
        angleTurret = Utils.angleToFromNegPiToPosPi(angleTurret);

        if(Gdx.input.isTouched()) {
            this.fire();
        }
        super.update(dt);
    }

    public void destroy() {
        this.lifeQuan --;
        this.hp = this.hpMax;
    }

//    private void fire (float dt) {
//
//        if(this.fireTimer >= this.weapon.getFirePeriod()) {
//            this.fireTimer = 0.0f;
//            float angleRadian = (float) Math.toRadians(this.angleTurret);
//            this.game.getBulletEmitter().activate(this, this.position.x, this.position.y, 320.0f * (float) Math.cos(angleRadian), 320.0f *(float) Math.sin(angleRadian), this.weapon.getDamage());
//        }
//    }

    public void checkMovement(float dt) {
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
//            this.position.x -= this.speed * dt;
//            this.angle = 180.0f;
            this.move(Direction.LEFT, dt);
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
//            this.position.x += this.speed * dt;
//            this.angle = 0.0f;
            this.move(Direction.RIGHT, dt);
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.UP)){
//            this.position.y += this.speed * dt;
//            this.angle = 90.0f;
            this.move(Direction.UP, dt);
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
//            this.position.y -= this.speed * dt;
//            this.angle = 270.0f;
            this.move(Direction.DOWN, dt);
        }
    }
}
