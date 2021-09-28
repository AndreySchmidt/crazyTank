package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.units.Tank;

public class BulletEmitter {

    private TextureRegion texture;
    private Bullet[] bullets;
    public static final int MAX_COUNT = 500;

    public BulletEmitter(TextureAtlas atlas) {
        this.texture = atlas.findRegion("projectile");
        this.bullets = new Bullet[MAX_COUNT];

        for (int i = 0; i < MAX_COUNT; i++) {
            this.bullets[i] = new Bullet();
        }
    }

    public Bullet[] getBullets() {
        return this.bullets;
    }

    public void activate(Tank owner, float x, float y, float speedX, float speedY, int damage, float maxTime) {
        for(int i = 0; i < this.bullets.length; i++) {
            // берем неактивную пулю и активирую её
            if(!this.bullets[i].isActive()){
                this.bullets[i].activate(owner, x, y, speedX, speedY, damage, maxTime);
                // выход из цикла, для активации только одной пули, а не всех неактивных
                break;
            }
        }
    }

    public void render(SpriteBatch batch) {
        // перебрать все буллеты
        for (int i = 0; i < this.bullets.length; i++) {
            if(bullets[i].isActive()) {
                batch.draw(texture, bullets[i].getPosition().x - 8, bullets[i].getPosition().y -8);
            }
        }
    }

    public void update(float dt) {
        for (int i = 0; i < bullets.length; i++) {
            if (bullets[i].isActive()) {
                bullets[i].update(dt);
            }
        }
    }

}
