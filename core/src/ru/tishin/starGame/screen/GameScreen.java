package ru.tishin.starGame.screen;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.tishin.starGame.game.GameController;
import ru.tishin.starGame.game.WorldRenderer;
import ru.tishin.starGame.screen.utils.Assets;

public class GameScreen extends AbstractScreen {
    private GameController controller;
    private WorldRenderer renderer;
    private Music music;

    public GameScreen(SpriteBatch batch) {
        super(batch);
    }

    @Override
    public void show() {
        Assets.getInstance().loadAssets(ScreenManager.ScreenType.GAME);
        this.controller = new GameController(batch); // когда откроется экран, инициализируем контр-р и рендерер
        this.renderer = new WorldRenderer(controller, batch);
        this.music = Assets.getInstance().getAssetManager().get("audio/mortal.mp3");
        music.setLooping(true);
        music.play();
    }

    @Override
    public void render(float delta) {
        controller.update(delta);
        renderer.render();
    }

    @Override
    public void dispose() {
        controller.dispose();
    }

    public Music getMusic() {
        return music;
    }

    @Override
    public void pause() {
        music.pause();
    }

    @Override
    public void resume() {
        music.play();
    }
}
