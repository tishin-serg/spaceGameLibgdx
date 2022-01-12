package ru.tishin.starGame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.StringBuilder;
import ru.tishin.starGame.screen.ScreenManager;
import ru.tishin.starGame.screen.utils.Assets;

/*
В этом классе есть ссылка на контроллер, который отдает ссылки на игровые сущности.
Метод render() будет отрисовывать эти сущности.
 */

public class WorldRenderer {
    private GameController controller;
    private SpriteBatch batch;
    private BitmapFont font32;
    private StringBuilder stringBuilder;

    public WorldRenderer(GameController controller, SpriteBatch batch) {
        this.controller = controller;
        this.batch = batch;
        this.font32 = Assets.getInstance().getAssetManager().get("fonts/font32.ttf", BitmapFont.class);
        this.stringBuilder = new StringBuilder();
    }

    public void render () {
        float deltaTime = Gdx.graphics.getDeltaTime();
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();
        controller.getBackground().render(batch);
        controller.getAsteroidController().render(batch);
        controller.getBulletController().render(batch);
        controller.getHero().render(batch);
        stringBuilder.clear();
        stringBuilder.append("SCORE: ").append(controller.getHero().getScoreView());
        font32.draw(batch, stringBuilder, 50, ScreenManager.SCREEN_HEIGHT - 50);
        stringBuilder.clear();
        stringBuilder.append("HP: ").append(controller.getHero().getHp());
        font32.draw(batch, stringBuilder, 50, ScreenManager.SCREEN_HEIGHT - 100);
        batch.end();
    }
}
