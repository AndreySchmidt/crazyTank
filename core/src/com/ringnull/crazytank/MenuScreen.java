package com.ringnull.crazytank;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MenuScreen extends AbstractScreen{

    private SpriteBatch batch;
    private BitmapFont font24;
    // для работы с интерфейсом (кнопки, меню)
    private Stage stage;
    private TextureAtlas atlas;


    public MenuScreen(SpriteBatch batch){
        this.batch = batch;
    }

    // что делать когда экран показали
    @Override
    public void show() {
        this.atlas = new TextureAtlas("game.pack");
        this.font24 = new BitmapFont(Gdx.files.internal("font24.fnt"));

        // для меню и кнопок import com.badlogic.gdx.scenes.scene2d.Stage; Есть еще другой стейдж от яваФХ
        this.stage = new Stage();
        // как выглядят элементы управления (набор обложек)
        Skin skin = new Skin();
        // добавляем картинку
        skin.add("simpleButton", new TextureRegion(atlas.findRegion("SimpleButton")));
        // создаем стиль кнопки
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        // указываем стилю как выглядет кнопка и буквы (.up как выглядит кнопка когда она отжата)
        textButtonStyle.up = skin.getDrawable("simpleButton");
        textButtonStyle.font = this.font24;

        // создаем группу для кнопок
        Group groupBtn = new Group();

        // создаем кнопку pause
        TextButton startBtn = new TextButton("start", textButtonStyle);
        // создаем кнопку exit
        TextButton exitBtn = new TextButton("exit", textButtonStyle);

        // повесим на нее действие
        startBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // перейти на экран игры
                ScreenManager.getInstance().setScreen(ScreenManager.ScreenType.GAME);
            }
        });

        exitBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // завершение работы
                Gdx.app.exit();
            }
        });

        startBtn.setPosition(0, 40);
        exitBtn.setPosition(0, 0);

        // добавить кнопки в группу (можно вывести и без группы на экран)
        groupBtn.addActor(startBtn);
        groupBtn.addActor(exitBtn);

        // координаты группы на окне
        groupBtn.setPosition(600, 300);

        // добавить группу на сцену
        stage.addActor(groupBtn);

        // чтобы система управления libGdx могла реагировать на stage (то есть сцена должна реагировать на действия пользователя)
        Gdx.input.setInputProcessor(stage);
    }

    // стандартный игровой цикл
    @Override
    public void render(float delta) {
        this.update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // отрисовка интерфейса кнопок
        this.stage.draw();// его можно за пределами batch.end(); вызывать не обязательно внутри

    }

    public void update (float dt) {
        // это кнопки
        this.stage.act(dt);
    }

    // при переходе с экрана на экран выполняется отчистка ресурсов
    @Override
    public void dispose() {
        this.font24.dispose();
        this.atlas.dispose();
        this.stage.dispose();
    }
}
