package com.ringnull.crazytank;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ringnull.crazytank.units.BotTank;
import com.ringnull.crazytank.units.PlayerTank;
import com.ringnull.crazytank.units.Tank;

public class GameScreen extends AbstractScreen {

    private SpriteBatch batch;
    private BitmapFont font24;
    private TextureAtlas atlas;
    private PlayerTank player;
    private BulletEmitter bulletEmitter;
    private BotEmitter botEmitter;
    private Map map;
    // таймер который сбрасывается
    private float gameTimer = 0.0f;
    // нужен таймер который не сбрасывается
    private float worldTimer = 0.0f;
    private static final boolean FRIENDLY_FIRE = false;
    // для работы с интерфейсом (кнопки, меню)
    private Stage stage;
    // остановлена игра
    private boolean paused = false;
    // для хранения временных данных при вычислениях
    private Vector2 mousePosition;
    // текстура курсора мыши
    private TextureRegion cursor;

    // подключить файл музыки разница в том, что музыка грузится с диска потоковое воспроизведение, а звук грузится в оперативу
//    private Music music;
    // подключить файл звука (звук грузится в оперативу)
//    private Sound sound;


    public GameScreen(SpriteBatch batch){
        this.batch = batch;
    }

    public BulletEmitter getBulletEmitter () {
        return this.bulletEmitter;
    }

    public Map getMap() {
        return this.map;
    }

    public PlayerTank getPlayer() {
        return this.player;
    }

    public Vector2 getMousePosition() {
        return this.mousePosition;
    }

    // что делать когда экран показали
    @Override
    public void show() {
//        this.sound = new Gdx.audio.newSound(Gdx.files.internal("someFile.wav"));
//        this.sound.play();
//        this.music = new Gdx.audio.music.newMusic(Gdx.files.internal("music.mp3"));
//        this.music.play();

        mousePosition = new Vector2();
        this.atlas = new TextureAtlas("game.pack");
        this.font24 = new BitmapFont(Gdx.files.internal("font24.fnt"));
        this.cursor = new TextureRegion(atlas.findRegion("cursor"));
        // зачистить атлас
//		atlas.dispose();
//        this.batch = new SpriteBatch();
        this.player = new PlayerTank(this, atlas);
        this.bulletEmitter = new BulletEmitter(atlas);
        this.botEmitter = new BotEmitter(this, atlas);
        this.map = new Map(atlas);

        // при запуске чтобы не ждать таймер создаем одного бота
//		this.botEmitter.activate(MathUtils.random(0, Gdx.graphics.getWidth()), MathUtils.random(0, Gdx.graphics.getHeight()));
        // чтобы не ждать бота сразу зададим таймер будто уже 6 секунд ждем
        this.gameTimer = 6.0f;

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
        TextButton pauseBtn = new TextButton("pause", textButtonStyle);
        // создаем кнопку exit
        TextButton exitBtn = new TextButton("exit", textButtonStyle);

        // повесим на нее действие
        pauseBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                paused = !paused;
            }
        });

        exitBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // завершение работы
                Gdx.app.exit();
            }
        });

//        pauseBtn.setPosition(800, 680);
        pauseBtn.setPosition(0, 40);
        exitBtn.setPosition(0, 0);

        // добавить кнопки в группу (можно вывести и без группы на экран)
        groupBtn.addActor(pauseBtn);
        groupBtn.addActor(exitBtn);

        // координаты группы на окне
        groupBtn.setPosition(1130, 640);

        // добавить группу на сцену
        stage.addActor(groupBtn);

        // добавить кнопку паузы на сцену
        // stage.addActor(pauseBtn);

        // чтобы система управления libGdx могла реагировать на stage (то есть сцена должна реагировать на действия пользователя)
        Gdx.input.setInputProcessor(stage);
        // отключить отображение системного курсора мышки (за пределами экрана он тоже отключается, даже если выходит за пределы окна)
//        Gdx.input.setCursorCatched(true);
    }

    // стандартный игровой цикл
    @Override
    public void render(float delta) {
        // получить у видеокарты время, которое прошло с момента последнего обновления
        // (чтобы количество пройденного пути не зависело от мощности чипсета,
        // ибо чем мощнее, тем чаще обновление кадров, если тормозит - то реже)
//        float dt = Gdx.graphics.getDeltaTime();

//        this.update(dt);
        this.update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // если хочешь чтобы камера следила за игроком, то переносим из ScreenManager init настройки камеры
//        ScreenManager.getInstance().getCamera().position.set(this.player.getPosition().x, this.player.getPosition().y, 0);
//        ScreenManager.getInstance().getCamera().update();
        // подвязываем камеру к игре (батч рисует то что видит камера)
        batch.setProjectionMatrix(ScreenManager.getInstance().getCamera().combined);

        batch.begin(); // таких блоков для отрисовки мира может быть несколько batch.begin(); batch.end();
        this.map.render(this.batch);
        this.player.render(this.batch);
        this.botEmitter.render(this.batch);
        this.bulletEmitter.render(batch);
        this.player.renderScore(this.batch, this.font24);
        // рисуем текстуру курсора (размер текстуры 48 на 48) рисуем по центру
//        batch.draw(this.cursor, this.mousePosition.x - 24, this.mousePosition.y - 24);
        // this.cursor.getRegionHeight() / 2 = 24
        // this.cursor.getRegionHeight() / 2 в 4 и 5 аргументе это якорь для вращения с целью анимации картинки
        // последний аргумент угол поворота картинки (воткнем туда worldTimer который не сбрасывается умножив его на 45 для ускорения вращения, минус для вращения по часовой стрелке)
        batch.draw(this.cursor, this.mousePosition.x - this.cursor.getRegionWidth() / 2, this.mousePosition.y - this.cursor.getRegionHeight() / 2, this.cursor.getRegionWidth() / 2, this.cursor.getRegionHeight() / 2, this.cursor.getRegionWidth(), this.cursor.getRegionHeight(), 1, 1, - this.worldTimer * 45);
        batch.end();
        // отрисовка интерфейса кнопок
        this.stage.draw();// его можно за пределами batch.end(); вызывать не обязательно внутри

    }

    // при переходе с экрана на экран выполняется отчистка ресурсов
    @Override
    public void dispose() {
        //TODO спрайты, атласы задиспоузить освободив ресурсы
        this.font24.dispose();
        this.atlas.dispose();
        this.atlas.dispose();
    }

    public void update (float dt) {
        // накапливаем при каждом апдейте несбрасываемый таймер
        this.worldTimer += dt;
        // при каждом апдейте запрашиваем положение мыши относительно экрана без переворота оси у, это задача вьюпорт учесть оси координат без меня
        this.mousePosition.set(Gdx.input.getX(), Gdx.input.getY());
        // далее вьюпорт вернет нам координаты мыши относительно игрового мира
        ScreenManager.getInstance().getViewPort().unproject(this.mousePosition);

        if (!this.paused){
            this.gameTimer += dt;
            // через сколько секунд выпускать нового бота
            if(this.gameTimer > 5.0f){
                this.gameTimer = 0.0f;
                // активируй бота в случайных координатах через время gameTimer
//			this.botEmitter.activate(MathUtils.random(0, Gdx.graphics.getWidth()), MathUtils.random(0, Gdx.graphics.getHeight()));

                // генерим случайные координаты для активации бота до тех пор, пока  Map не скажет, что клетка для бота свободна на карте
                float coordX, coordY;
                do{
                    coordX = MathUtils.random(0, Gdx.graphics.getWidth());
                    coordY = MathUtils.random(0, Gdx.graphics.getHeight());
                    // надо убрать ширину бота 40 в константу
                } while (!this.map.isAreaClear(coordX, coordY, 40));

                // активируй бот
                this.botEmitter.activate(coordX, coordY);
            }

            this.player.update(dt);
            this.botEmitter.update(dt);
            this.bulletEmitter.update(dt);
            this.checkCollisions();
        }

        // это кнопки
        this.stage.act(dt);
    }

    // проверка столкновений танка с пулей
    public void checkCollisions(){
        // берем все пули
        for (int i = 0; i < this.bulletEmitter.getBullets().length; i++){
            Bullet bullet = this.bulletEmitter.getBullets()[i];

            // если пуля активна
            if(bullet.isActive()){
                if(this.checkBullet(player, bullet) && player.getCircle().contains(bullet.getPosition())) {
                    player.takeDamage(bullet.getDamage());
                    bullet.deactivate();
                    // пуля попала и деактивирована так что перебирать ботов не надо
                    continue;
                }

                // берем всех ботов
                for (int j = 0; j < this.botEmitter.getBots().length; j++){
                    BotTank botTank = this.botEmitter.getBots()[j];
                    // если бот активен
                    if(botTank.isActive()){
                        // contains (передаем координаты пули) содержит ли в себе переданный объект (botTank.getCircle().contains(bullet.getPosition()))
//						// дружественный огонь (бот попадает под пулю другого бота) bullet.getOwner() instanceof PlayerTank
//						// без дружественного огня (бот НЕ попадает под пулю другого бота) bullet.getOwner() != botTank
//						if(bullet.getOwner() instanceof PlayerTank && botTank.getCircle().contains(bullet.getPosition())){

                        // является ли пуля его и что делать this.checkBullet(botTank, bullet)
                        if(this.checkBullet(botTank, bullet) && botTank.getCircle().contains(bullet.getPosition())){
                            // боту наносим урон (сколько урона узнаем у пули)
                            botTank.takeDamage(bullet.getDamage());
                            // деактивируем попавшую в танк пулю
                            bullet.deactivate();
                            // чтобы пуля не нанесла дамаг другим танкам (можно доработать это, например бомба, которая поражает все танки в радиусе, а не один как сейчас)
                            break;
                        }
                    }
                }
                // проверка столкновения пули с препятствием
                map.checkWallCollision(bullet);
            }
        }
    }

    // если дружеский огонь включен, то бот убивает бота при попадании
    public boolean checkBullet(Tank tank, Bullet bullet){
        if(FRIENDLY_FIRE){
            // танк не должен попадать по себе
            return tank != bullet.getOwner();
        } else {
            // танк не должен попадать по танку своего типа (пока 2 типа PLAYER и AI)
            return tank.getOwnerType() != bullet.getOwner().getOwnerType();
        }
        // реализация метода без enum
        //		if(FRIENDLY_FIRE) {
        //			return bullet.getOwner() != bot && bot.getCircle().contains(bullet.getPosition());
        //		}else{
        //			return bullet.getOwner() instanceof PlayerTank && bot.getCircle().contains(bullet.getPosition());
        //		}
    }
}
