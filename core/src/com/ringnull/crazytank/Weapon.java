package com.ringnull.crazytank;

//import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class Weapon {

    private TextureRegion texture;
    private int damage;
    private float firePeriod;
    // радиус атаки ствола
    private float radius;
    // скорость полета пули
    private float projectileSpeed;
    // время жизни пули
    private float projectileLifeTime;

    public Weapon(TextureAtlas atlas){
        this.texture = atlas.findRegion("simpleWeapon");
        this.firePeriod = 0.4f;
        this.damage = 1;
        this.radius = 400.0f;
        this.projectileSpeed = 320.0f;
        this.projectileLifeTime = this.radius / this.projectileSpeed;
    }

    public float getProjectileSpeed() {
        return this.projectileSpeed;
    }

    public float getProjectileLifeTime() {
        return this.projectileLifeTime;
    }

    public float getRadius() {
        return this.radius;
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
}
