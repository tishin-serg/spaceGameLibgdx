package ru.tishin.starGame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

/*
В этом классе есть ссылка на контроллер, который отдает ссылки на игровые сущности.
Метод render() будет отрисовывать эти сущности.
 */

public class WorldRenderer {
    private GameController controller;
    private SpriteBatch batch;

    public WorldRenderer(GameController controller, SpriteBatch batch) {
        this.controller = controller;
        this.batch = batch;
    }

    public void render () {
        float deltaTime = Gdx.graphics.getDeltaTime();
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();
        controller.getBackground().render(batch);
        controller.getHero().render(batch);
        controller.getAsteroidController().render(batch);
        controller.getBulletController().render(batch);
        batch.end();
    }
}
