package com.ringnull.crazytank;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureArray;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ringnull.crazytank.units.Tank;

public class BulletEmitter {

    private TextureRegion bulletTexture;
    private Bullet[] bullets;
    public static final int MAX_BULLETS_COUNT = 10;

    public BulletEmitter(TextureAtlas atlas){
        this.bulletTexture = atlas.findRegion("projectile");

        // инициализация массива пуль
        this.bullets = new Bullet[MAX_BULLETS_COUNT];
        for (int i = 0; i < MAX_BULLETS_COUNT; i++){
            this.bullets[i] = new Bullet();
        }
    }

    public Bullet[] getBullets() {
        return this.bullets;
    }

    public void activate (Tank owner, float x, float y, float vx, float vy, int damage, float maxTime) {
        for(int i = 0; i < this.bullets.length; i++){
            if(!this.bullets[i].isActive()){
                this.bullets[i].activate(owner, x, y, vx, vy, damage, maxTime);
                break;
            }
        }
    }

    public void render(SpriteBatch batch){
        for(int i = 0; i < this.bullets.length; i++){
            if(this.bullets[i].isActive()){
                batch.draw(this.bulletTexture, this.bullets[i].getPosition().x - 8, this.bullets[i].getPosition().y - 8);
            }
        }
    }

    public void update(float dt){
        for(int i = 0; i < this.bullets.length; i++){
            if(this.bullets[i].isActive()){
                this.bullets[i].update(dt);
            }
        }
    }
}
