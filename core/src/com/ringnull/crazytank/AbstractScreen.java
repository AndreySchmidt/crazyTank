package com.ringnull.crazytank;

import com.badlogic.gdx.Screen;

// вынесем сюда мусор из GameScreen они у разных экранов не отличаются
public abstract class AbstractScreen implements Screen {
    // поменяли размер экрана
    @Override
    public void resize(int width, int height) {
        // при смене размеров вызови ресайз у скринменеджера
        ScreenManager.getInstance().resize(width, height);
    }

    @Override
    public void pause() {

    }

    // развернули обратно экран
    @Override
    public void resume() {

    }

    // окно скрылось
    @Override
    public void hide() {

    }
}
