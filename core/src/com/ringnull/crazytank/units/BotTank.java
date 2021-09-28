package com.ringnull.crazytank.units;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.ringnull.crazytank.GameScreen;
import com.ringnull.crazytank.Weapon;
import com.ringnull.crazytank.crazyTank;
import com.ringnull.crazytank.utils.Direction;
import com.ringnull.crazytank.utils.TankOwner;

public class BotTank extends Tank{

    public Direction preferredDirection;
    private float aiTimer;
    // изменить поведение через aiTimerTo
    private float aiTimerTo;
    private boolean active = false;

    // радиус реакции бота на танк плеера
    private float pursuitRadius;

    // чтобы бот не залипал
    Vector3 lastPosition;

    public BotTank(GameScreen gameScreen, TextureAtlas atlas) {
        super(gameScreen);
        this.texture = atlas.findRegion("botTankBase");
        this.textureHp = atlas.findRegion("bar");
        this.weapon = new Weapon(atlas);

        this.position = new Vector2(500.0f, 500.0f);

        // x y нормальные координаты, а z время, которое он торчит у стены или иного препятствия
        this.lastPosition = new Vector3(0.0f,0.0f,0.0f);
        this.speed = 100;

//        this.width = texture.getWidth();
//        this.height = texture.getHeight();
        this.width = texture.getRegionWidth();
        this.height = texture.getRegionHeight();

        this.hpMax = 4;
        this.hp = hpMax;

        // измени свое поведение через 3 секунды
        this.aiTimerTo = 3.0f;

        this.preferredDirection = Direction.UP;
        this.circle = new Circle(this.position.x, this.position.y, (this.width + this.height) / 2);
        this.pursuitRadius = 400.0f;
        this.ownerType = TankOwner.AI;
    }

    @Override
    public void destroy() {
        this.active = false;
    }

    public boolean isActive() {
        return active;
    }

    // активировать бота по координатам
    public void activate(float x, float y){
        this.hp = 3;
        this.hpMax = this.hp;
        this.position.set(x, y);
        this.preferredDirection = Direction.values()[MathUtils.random(0, Direction.values().length - 1)];
        this.angle = this.preferredDirection.getAngle();
        this.aiTimer = 0.0f;
        this.active = true;
    }

    public void update (float dt) {
        // тикает время и сравним его со временем, когда надо сменить поведение бота
        this.aiTimer += dt;

        if (this.aiTimer >= this.aiTimerTo){
            // обнулить таймер бота
            this.aiTimer = 0.0f;
            // сгенерируй случайное время от 2,5 до 4 секунд для изменения периода следующего изменения поведения бота
            this.aiTimerTo = MathUtils.random(2.5f, 4.0f);
            // выбери случайное направление из массива enum
            this.preferredDirection = Direction.values()[MathUtils.random(0, Direction.values().length - 1)];
            this.angle = this.preferredDirection.getAngle();
        }

        // двигаем бот (this.preferredDirection.getVx() будет 0,1,-1 и тд, от этого зависит направление движения)
        // this.position.add(this.speed * this.preferredDirection.getVx() * dt, this.speed * this.preferredDirection.getVy() * dt);
        this.move(this.preferredDirection, dt);

        // берем позицию бота и считаем позицию до (передаем вектор игрока)
        float dst = this.position.dst(game.getPlayer().getPosition());
        // если расстояние между ботом и танком игрока меньше радиуса реакции на врага
        if(dst < this.pursuitRadius){
            // поворот турели бота в сторону танка игрока
            this.rotateTurret(game.getPlayer().getPosition().x, game.getPlayer().getPosition().y, dt);
            // огонь ботом по танку игрока
            this.fire();
        }

        // если бот стоит на месте Math.abs(this.position.x - this.lastPosition.x) < 0.5f разница между позицией и прошлой позицией меньше пол пикселя
        if(Math.abs(this.position.x - this.lastPosition.x) < 0.5f && Math.abs(this.position.y - this.lastPosition.y) < 0.5f){
            // добавим время указывающее сколько бот стоит на месте
            this.lastPosition.z += dt;
            // если бот стоит на месте дольше, чем треть секунды, то его aiTimer увеличить во избежание тормозов
            if(this.lastPosition.z < 0.3f){
                // накручиваем ему простой (якобы стоит долго, + 10 секунд)
                this.aiTimer += 10.0f;
            }
            // если бот двигается, то else
        } else {
            this.lastPosition.x = this.position.x;
            this.lastPosition.y = this.position.y;
            this.lastPosition.z = 0.0f;
        }


        super.update(dt);
    }
}
