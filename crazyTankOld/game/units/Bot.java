package com.mygdx.game.units;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Direction;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Weapon;

public class Bot extends Tank{
    Direction preferredDirection;
    private float aiTimer;
    private float aiTimerTo = 3.0f;
    private boolean active = false;
    private float pursuitRadius = 300.0f;
    private Vector3 lastPosition;

    public Bot(MyGdxGame game, TextureAtlas atlas) {
        super(game);
        this.texture = atlas.findRegion("botTankBase");
        this.textureHp = atlas.findRegion("bar");
        this.position = new Vector2(500.0f, 500.0f);
        this.speed = 100.0f;
        this.height = this.texture.getRegionHeight();
        this.width = this.texture.getRegionWidth();
        this.weapon = new Weapon(atlas);

        this.hpMax = 3;
        this.hp = this.hpMax;
        this.preferredDirection = Direction.UP;
        this.circle = new Circle(this.position.x, this.position.y, (this.width + this.height) / 2);
        this.lastPosition = new Vector3(0.0f, 0.0f, 0.0f);
    }

    public void update (float dt) {
        this.aiTimer += dt;
//        this.fireTimer += dt;

        if(this.aiTimer >= this.aiTimerTo) {
            this.aiTimer = 0.0f;
            this.aiTimerTo = MathUtils.random(3.5f, 6.0f);
            this.preferredDirection = Direction.values()[MathUtils.random(0, (Direction.values().length - 1))];
            this.angle = this.preferredDirection.getAngle();
        }
        this.move(this.preferredDirection, dt);
//        this.position.add(this.speed * this.preferredDirection.getVx() * dt, this.speed * this.preferredDirection.getVy() * dt);

        float dst = this.position.dst(this.game.getPlayerTank().getPosition());
        if(dst < this.pursuitRadius) {
            this.rotateTurretToPoint(this.game.getPlayerTank().getPosition().x, this.game.getPlayerTank().getPosition().y, dt);
            this.fire();
        }

        if(Math.abs(this.position.x - this.lastPosition.y) < 0.5f && Math.abs(this.position.y - this.lastPosition.y) < 0.5f)
        {
            this.lastPosition.z += dt;
            if(this.lastPosition.z > 0.3f)
            {
                this.aiTimer += 10.0f;
            }
        }
        else
        {
            this.lastPosition.x = this.position.x;
            this.lastPosition.y = this.position.y;
            this.lastPosition.z = 0.0f;
        }

        super.update(dt);
    }

//    private void fire (float dt) {
//        if(this.fireTimer >= this.weapon.getFirePeriod()) {
//            this.fireTimer = 0.0f;
//            float angleRadian = (float) Math.toRadians(this.angleTurret);
//            this.game.getBulletEmitter().activate(this.position.x, this.position.y, 320.0f * (float) Math.cos(angleRadian), 320.0f *(float) Math.sin(angleRadian), this.weapon.getDamage());
//        }
//    }

    public void destroy() {
        this.active = false;
    }

    public void activate(float x, float y) {
        this.active = true;
        this.hp = this.hpMax;
        this.position.set(x, y);
        this.preferredDirection = Direction.values()[MathUtils.random(0, (Direction.values().length - 1))];
        this.aiTimer = 0.0f;
        this.angle = this.preferredDirection.getAngle();
    }

    public boolean isActive() {
        return this.active;
    }
}
