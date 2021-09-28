package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.units.Bot;
import com.mygdx.game.units.PlayerTank;
import com.mygdx.game.units.Tank;


public class MyGdxGame extends ApplicationAdapter {
	private Map map;
	private SpriteBatch batch;
	private PlayerTank playerTank;
	private BulletEmitter bulletEmitter;
	private BotEmitter botEmitter;
	private float gameTimer = 0.0f;
	private static final boolean FRIENDLY_FIRE = false;

	public BulletEmitter getBulletEmitter() {
		return this.bulletEmitter;
	}

	@Override
	public void create () {
		TextureAtlas atlas = new TextureAtlas("game.pack");
		this.map = new Map(atlas);
		this.batch = new SpriteBatch();
		this.playerTank = new PlayerTank(this, atlas);
		this.bulletEmitter = new BulletEmitter(atlas);
		this.botEmitter = new BotEmitter(this, atlas);
		this.gameTimer = 6.0f;
//		this.botEmitter.activate(MathUtils.random(0, Gdx.graphics.getWidth()), MathUtils.random(0, Gdx.graphics.getHeight()));
	}

	public PlayerTank getPlayerTank() {
		return this.playerTank;
	}

	@Override
	public void render () {
		float dt = Gdx.graphics.getDeltaTime();

		this.update(dt);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		this.map.render(batch);
		this.playerTank.render(batch);
		this.botEmitter.render(batch);
		this.bulletEmitter.render(batch);
		batch.end();
	}

	public void update (float dt) {
		this.gameTimer += dt;
		if(this.gameTimer >= 5.0f) {
			this.gameTimer = 0.0f;

			float coordX, coordY;
			do {
				coordX = MathUtils.random(0, Gdx.graphics.getWidth());
				coordY = MathUtils.random(0, Gdx.graphics.getHeight());
			} while(!this.map.isAreaEmpty(coordX, coordY, 20));

			this.botEmitter.activate(coordX, coordY);
		}

		this.playerTank.update(dt);
		this.botEmitter.update(dt);
		this.bulletEmitter.update(dt);

		this.checkCollision();
	}

	public void checkCollision() {
		for (int i = 0; i < this.bulletEmitter.getBullets().length; i++) {
			Bullet bullet = this.bulletEmitter.getBullets()[i];
			if(bullet.isActive()) {
				if(this.map.checkWallBulletCollision(bullet))
				{
					continue;
				}

				if(!(bullet.getOwner() instanceof PlayerTank) && playerTank.getCircle().contains(bullet.getPosition())) {
					playerTank.takeDamage(bullet.getDamage());
					bullet.deactivate();
					continue;
				}

				for (int j = 0; j < this.botEmitter.getBots().length; j++) {
					Bot bot = this.botEmitter.getBots()[j];
					if(bot.isActive()) {
						if(checkBulletOwner(bot, bullet)) {
							bot.takeDamage(bullet.getDamage());
							bullet.deactivate();
							break;
						}
					}
				}
			}
		}
	}

	public boolean checkBulletOwner(Tank bot, Bullet bullet) {
		if(FRIENDLY_FIRE) {
			return bullet.getOwner() != bot && bot.getCircle().contains(bullet.getPosition());
		}else{
			return bullet.getOwner() instanceof PlayerTank && bot.getCircle().contains(bullet.getPosition());
		}
	}

	@Override
	public void dispose () {
		batch.dispose();
	}

	public Map getMap(){
	    return this.map;
    }
}
