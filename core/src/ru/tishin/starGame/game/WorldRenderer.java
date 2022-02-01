package ru.tishin.starGame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
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
    private BitmapFont font72;
    private StringBuilder sb;

    public WorldRenderer(GameController controller, SpriteBatch batch) {
        this.controller = controller;
        this.batch = batch;
        this.font32 = Assets.getInstance().getAssetManager().get("fonts/font32.ttf", BitmapFont.class);
        this.font72 = Assets.getInstance().getAssetManager().get("fonts/font72.ttf", BitmapFont.class);
        this.sb = new StringBuilder();
    }

    public void render () {
        float deltaTime = Gdx.graphics.getDeltaTime();
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();
        controller.getBackground().render(batch);
        controller.getAsteroidController().render(batch);
        controller.getBulletController().render(batch);
        controller.getHero().render(batch);
        controller.getHero().renderGUI(batch, font32);
        controller.getParticleController().render(batch);
        controller.getBonusController().render(batch);
        controller.getInfoController().render(batch, font32);
        controller.getBotController().render(batch);
        if (controller.getTimer() < 3) {
            sb.clear();
            sb.append("Level ").append(controller.getLevel().getCurrentLevel());
            font72.draw(batch, sb, 0, ScreenManager.SCREEN_HALF_HEIGHT, ScreenManager.SCREEN_WIDTH,
                     Align.center, false);
        }
        batch.end();
        controller.getStage().draw();
    }
}
