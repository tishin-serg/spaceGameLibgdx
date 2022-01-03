package ru.tishin.starGame.screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.tishin.starGame.game.GameController;
import ru.tishin.starGame.game.WorldRenderer;
import ru.tishin.starGame.screen.utils.Assets;

public class GameScreen extends AbstractScreen{
    private SpriteBatch batch;
    private GameController controller;
    private WorldRenderer renderer;

    public GameScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    @Override
    public void show() {
        Assets.getInstance().loadAssets(ScreenManager.ScreenType.GAME);
        this.controller = new GameController(); // когда откроется экран, инициализируем контр-р и рендерер
        this.renderer = new WorldRenderer(controller, batch);
    }

    @Override
    public void render(float delta) {
        controller.update(delta);
        renderer.render();
    }
}
