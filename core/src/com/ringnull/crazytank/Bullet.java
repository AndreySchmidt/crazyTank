package com.ringnull.crazytank;

import com.badlogic.gdx.math.Vector2;
import com.ringnull.crazytank.units.Tank;

public class Bullet {
    // чья пуля
    private Tank owner;
    private Vector2 position;
    private Vector2 velocity;
    private boolean active;
    private int damage = 0;

    //добавим пуле время жизни для ограничения дальности стрельбы
    private float currentTime;
    private float maxTime;

    public Bullet () {
        this.position = new Vector2(0.0f, 0.0f);
        this.velocity = new Vector2(0.0f, 0.0f);
        this.active = false;
    }

    public Tank getOwner() {
        return this.owner;
    }

    public int getDamage() {
        return this.damage;
    }

    public Vector2 getPosition() {
        return position;
    }

    public boolean isActive () {
        return this.active;
    }

    public void activate (Tank owner, float x, float y, float vx, float vy, int damage, float maxTime) {
        this.owner = owner;
        this.position.set(x, y);
        this.velocity.set(vx, vy);
        this.active = true;
        this.damage = damage;
        this.maxTime = maxTime;
        this.currentTime = 0.0f;
    }

    public void deactivate () {
        this.active = false;
    }

    public void update (float dt) {
        /*
           сумма векторов с умножением на коэффициент
           берем вектор скорости умножаем на dt и прибавляем к позишн
            position.mulAdd(velocity, dt);

            this.position.x += this.velocity.x * dt;
            this.position.y += this.velocity.y * dt;
        */
        // копим время жизни пули
        this.currentTime += dt;
        // если время жизни пули закончилось, то деактивировать ее
        if(this.currentTime > this.maxTime){
            this.currentTime = 0.0f;
            this.deactivate();
        }

        position.mulAdd(velocity, dt);
        // снаряд вылетел за пределы экрана деактивируем его
        if (this.position.x < 0.0f || this.position.x > 1280.0f || this.position.y < 0.0f || this.position.y > 720.0f) {
            this.deactivate();
        }
    }
}
