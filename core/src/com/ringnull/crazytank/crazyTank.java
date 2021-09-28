package com.ringnull.crazytank;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


//public class crazyTank extends ApplicationAdapter {
public class crazyTank extends Game {
	private SpriteBatch batch;

	@Override
	public void create () {
		SpriteBatch batch = new SpriteBatch();
		ScreenManager.getInstance().init(this, batch);
		// перейти на экран игры
//		ScreenManager.getInstance().setScreen(ScreenManager.ScreenType.GAME);
		// перейти на экран меню
		ScreenManager.getInstance().setScreen(ScreenManager.ScreenType.MENU);
	}

	@Override
	public void render () {
		// получить у видеокарты время, которое прошло с момента последнего обновления
		// (чтобы количество пройденного пути не зависело от мощности чипсета,
		// ибо чем мощнее, тем чаще обновление кадров, если тормозит - то реже)
        float dt = Gdx.graphics.getDeltaTime();
		// получить ссылку на текущий экран и выполнить у него рендер с dt
		getScreen().render(dt);
	}
	
	@Override
	public void dispose () {
		this.batch.dispose();
	}
}
