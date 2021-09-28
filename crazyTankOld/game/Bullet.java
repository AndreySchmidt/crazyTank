package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.units.Tank;

public class Bullet {
//    private Texture texture;
//    private float x;
//    private float y;
//    private float speedX;
//    private float speedY;
    private Vector2 position;
    private Vector2 velocity;
    private boolean active;
    private int damage = 0;
    private Tank owner;
    private float currentTime;
    private float maxTime;


    public Bullet () {
//        this.texture = new Texture("projectile.png");
//        this.texture = new Texture("bullet.png");
        this.position = new Vector2();
        this.velocity = new Vector2();
//        this.x = 0.0f;
//        this.y = 0.0f;
//        this.speedX = 0.0f;
//        this.speedY = 0.0f;
        this.active = false;
    }

    public int getDamage() {
        return this.damage;
    }

    public Tank getOwner() {
        return this.owner;
    }

    public Vector2 getPosition() {
        return this.position;
    }

    public boolean isActive () {
        return this.active;
    }

//    public void render (SpriteBatch batch) {
//        batch.draw(texture, position.x-8, position.y-8);
//    }

    public void activate (Tank owner, float x, float y, float speedX, float speedY, int damage, float maxTime) {
//        this.x = x;
//        this.y = y;
//        this.speedX = speedX;
//        this.speedY = speedY;
        this.owner = owner;
        this.position.set(x, y);
        this.velocity.set(speedX, speedY);
        this.damage = damage;
        this.active = true;
        this.maxTime = maxTime;
        this.currentTime = 0.0f;
    }
    public void deactivate () {
        this.active = false;
    }

    public void update (float dt) {
        this.currentTime += dt;
        if(this.currentTime >= this.maxTime)
        {
            this.deactivate();
        }
//      сумма векторов  position.add(velocity) если просто сумма но у нас множитель * dt
//        x += speedX * dt;
//        y += speedY * dt;

        // поэтому мультисуммирование векторов
        this.position.mulAdd(this.velocity, dt);

        if(position.x < 0.0f || position.x > Gdx.graphics.getWidth() || position.y < 0.0f || position.y > Gdx.graphics.getHeight()) {
            this.deactivate();
        }
    }
}
