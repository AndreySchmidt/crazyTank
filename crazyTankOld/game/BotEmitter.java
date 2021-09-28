package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.game.units.Bot;

public class BotEmitter {
    private Bot[] bots;
    public static final int MAX_COUNT = 5;

    public BotEmitter(MyGdxGame game, TextureAtlas atlas) {
        this.bots = new Bot[MAX_COUNT];

        for (int i = 0; i < MAX_COUNT; i++) {
            this.bots[i] = new Bot(game, atlas);
        }
    }

    public Bot[] getBots() {
        return this.bots;
    }

    public void activate(float x, float y) {
        for(int i = 0; i < this.bots.length; i++) {
            if(!this.bots[i].isActive()){
                this.bots[i].activate(x, y);
                break;
            }
        }
    }

    public void render(SpriteBatch batch) {
        // перебрать все буллеты
        for (int i = 0; i < this.bots.length; i++) {
            if(this.bots[i].isActive()) {
                this.bots[i].render(batch);
            }
        }
    }

    public void update(float dt) {
        for (int i = 0; i < this.bots.length; i++) {
            if (this.bots[i].isActive()) {
                this.bots[i].update(dt);
            }
        }
    }
}
