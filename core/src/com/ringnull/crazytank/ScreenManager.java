package com.ringnull.crazytank;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class ScreenManager {

    // ссылка на игру
    private Game game;
    private MenuScreen menuScreen;
    private GameScreen gameScreen;

    // ViewPort он нужен для масштабирования игра под разные размеры экранов (окно можно растянуть или уменьшить, от этого меняется отображение в игре)
    // в нем информация каким образом наш мир подстраивается под окно
//    private ViewPort viewPort; почему-то не получается, видимо из - за типизации (из-за импорта другого класса, внимательнее тут импортировать import com.badlogic.gdx.utils.viewport.Viewport;)
//    private FitViewport viewPort;
    private Viewport viewPort;
    // для слежения за участком экрана, например игроком, если карта больше экрана
    private Camera camera;

    // длина и высота мира
    public static final int WORLD_WIDTH = 1280;
    public static final int WORLD_HEIGHT = 720;

    public enum ScreenType {
        MENU, // меню
        GAME // экран игры
    }

    private static ScreenManager ourInstance = new ScreenManager();

    public static ScreenManager getInstance(){
        return  ourInstance;
    }

    private ScreenManager(){

    }

    public Viewport getViewPort() {
        return this.viewPort;
    }

    public Camera getCamera() {
        return this.camera;
    }

    public void init (Game game, SpriteBatch batch){
        this.game = game;
        this.gameScreen = new GameScreen(batch);
        this.menuScreen = new MenuScreen(batch);

        // создаем одну камеру и один вьюпорт на весь проект
        // работаем в двухмерном пространстве, OrthographicCamera смотрит сверху
        this.camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
        // камера, ты смотришь в центр нашего мира (WORLD_WIDTH / 2), (WORLD_HEIGHT / 2)
        this.camera.position.set((WORLD_WIDTH / 2), (WORLD_HEIGHT / 2), 0);
        // каждый раз при изменении позиции position камеры надо делать update апдейт
        this.camera.update();

        // вьюпорт бывает разный (идея вьюпорт - подгонка картинки под различные экраны) , FitViewport подгонка с сохранением пропорций сторон
        this.viewPort = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, this.camera);
    }

    // для пересчета мира под размер экрана при ресайзе (float width, float height - новые размеры окна при изменении)
    // при ресайзе координаты мыши тоже надо пересчитать (есть батч и координаты в пределах батча, да еще и измененного по масштабу)
    public void resize(int width, int height){
        // поменять ширину, длину мира игры под окно
        this.viewPort.update(width, height);
        // применить изменения пересчитай картинку под экран
        this.viewPort.apply();
    }

    // для перехода между экранами
    public void setScreen(ScreenType screenType){
        // запомним текущий экран (игра на каком экране мы были?)
        Screen currentScreen = game.getScreen();

        // на какой экран запрос на переход
        switch (screenType){
            case GAME:
                // игра, твой текущий экран будет gameScreen
                game.setScreen(gameScreen);
                break;
            case MENU:
                // игра, твой текущий экран будет menuScreen
                game.setScreen(menuScreen);
                break;
        }

        // если текущий экран с которого мы переходили не равен null (может мы только начали игру и у нас нет currentScreen)
        if (currentScreen != null){
            // освободить ресурсы в currentScreen
            currentScreen.dispose();
        }
    }
}
