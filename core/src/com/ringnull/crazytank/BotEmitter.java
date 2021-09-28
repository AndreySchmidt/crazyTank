package com.ringnull.crazytank;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.ringnull.crazytank.units.BotTank;

public class BotEmitter {

    private BotTank[] bots;
    public static final int MAX_BOTS_COUNT = 4;


    public BotEmitter(GameScreen gameScreen, TextureAtlas atlas){
        this.bots = new BotTank[MAX_BOTS_COUNT];
        for (int i = 0; i < MAX_BOTS_COUNT; i++){
            this.bots[i] = new BotTank(gameScreen, atlas);
        }
    }

    public BotTank[] getBots() {
        return bots;
    }

    public void activate (float x, float y) {
        for(int i = 0; i < this.bots.length; i++){
            if(!this.bots[i].isActive()){
                this.bots[i].activate(x, y);
                break;
            }
        }
    }

    public void render(SpriteBatch batch){
        for(int i = 0; i < this.bots.length; i++){
            if(this.bots[i].isActive()){
                this.bots[i].render(batch);
            }
        }
    }

    public void update(float dt){
        for(int i = 0; i < this.bots.length; i++){
            if(this.bots[i].isActive()){
                this.bots[i].update(dt);
            }
        }
    }
}
