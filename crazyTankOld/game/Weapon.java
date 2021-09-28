package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Weapon {
    private TextureRegion texture;
    private int damage = 1;
    private float radius = 300.0f;
    private float projectileSpeed = 320.0f;
    private float projectileLifeTime;
//    private float fireTimer = 0.0f;
    private float firePeriod = 0.5f; // чем больше, тем менее интенсивный огонь

    public Weapon(TextureAtlas atlas) {
        this.texture = atlas.findRegion("simpleWeapon");
        this.projectileLifeTime = this.radius / this.projectileSpeed;
    }

    public TextureRegion getTexture() {
        return this.texture;
    }

    public int getDamage() {
        return this.damage;
    }

    public float getFirePeriod() {
        return this.firePeriod;
    }

    public float getRadius() {
        return this.radius;
    }

    public float getProjectileSpeed() {
        return this.projectileSpeed;
    }

    public float getProjectileLifeTime() {
        return this.projectileLifeTime;
    }
}
