package ru.tishin.starGame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.StringBuilder;
import ru.tishin.starGame.game.Background;
import ru.tishin.starGame.game.Hero;
import ru.tishin.starGame.screen.utils.Assets;

public class GameOverScreen extends AbstractScreen {
    private BitmapFont font72;
    private BitmapFont font32;
    private Stage stage;
    private StringBuilder stringBuilder;
    private Hero deadHero;
    private Background background;

    public GameOverScreen(SpriteBatch batch) {
        super(batch);
    }

    @Override
    public void show() {
        this.background = new Background(null);
        this.stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        this.font72 = Assets.getInstance().getAssetManager().get("fonts/font72.ttf");
        this.font32 = Assets.getInstance().getAssetManager().get("fonts/font32.ttf");
        this.stringBuilder = new StringBuilder();

        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("simpleButton");
        textButtonStyle.font = font32;
        skin.add("simpleSkin", textButtonStyle);

        Button btnMenu = new TextButton("Menu", textButtonStyle);
        btnMenu.setPosition(480, 110);

        btnMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
            }
        });

        stage.addActor(btnMenu);
        skin.dispose();
    }

    public void update(float dt) {
        background.update(dt);
        stage.act(dt);
    }

    @Override
    public void render(float delta) {
        stringBuilder.clear();
        update(delta);
        ScreenUtils.clear(0f, 0f, 0f, 1);
        batch.begin();
        background.render(batch);
        font72.draw(batch, "GAME OVER", 0, 600, ScreenManager.SCREEN_WIDTH, 1, false);
        stringBuilder.append("SCORE: ").append(deadHero.getScore()).append("\n");
        stringBuilder.append("COINS: ").append(deadHero.getCoins());
        font32.draw(batch, stringBuilder, 0, 500, ScreenManager.SCREEN_WIDTH, 1, false);
        batch.end();
        stage.draw();
    }

    public void saveDeadHero(Hero hero) {
        this.deadHero = hero;
    }
}
