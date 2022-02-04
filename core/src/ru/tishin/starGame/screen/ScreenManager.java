package ru.tishin.starGame.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ru.tishin.starGame.StarGame;
import ru.tishin.starGame.game.Hero;
import ru.tishin.starGame.screen.utils.Assets;

public class ScreenManager {

    public enum ScreenType {
        GAME, MENU, GAME_OVER
    }

    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
    public static final int SCREEN_HALF_WIDTH = 640;
    public static final int SCREEN_HALF_HEIGHT = 360;

    private SpriteBatch batch;
    private StarGame game;
    private GameScreen gameScreen;
    private LoadingScreen loadingScreen;
    private MenuScreen menuScreen;
    private GameOverScreen gameOverScreen;
    private Screen targetScreen;
    private Viewport viewport;

    private static ScreenManager ourInstance = new ScreenManager();

    public static ScreenManager getInstance() {
        return ourInstance;
    }

    public Viewport getViewport() {
        return viewport;
    }

    private ScreenManager() {

    }

    public void init(StarGame game, SpriteBatch batch) {
        this.game = game;
        this.batch = batch;
        this.viewport = new FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT);
        this.gameScreen = new GameScreen(batch);
        this.loadingScreen = new LoadingScreen(batch);
        this.menuScreen = new MenuScreen(batch);
        this.gameOverScreen = new GameOverScreen(batch);
    }

    public void saveDeadHero(Hero hero) {
        gameOverScreen.saveDeadHero(hero);
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
        viewport.apply();
    }

    public void changeScreen(ScreenType type) {
        Screen screen = game.getScreen();
        Assets.getInstance().clear();
        if(screen != null) screen.dispose();
        game.setScreen(loadingScreen);

        switch (type) {
            case GAME:
                targetScreen = gameScreen;
                Assets.getInstance().loadAssets(ScreenType.GAME);
                break;
            case MENU:
                targetScreen = menuScreen;
                Assets.getInstance().loadAssets(ScreenType.MENU);
                break;
            case GAME_OVER:
                targetScreen = gameOverScreen;
                Assets.getInstance().loadAssets(ScreenType.GAME_OVER);
                break;
        }
    }

    public void goToTarget() {
        game.setScreen(targetScreen);
    }

    public Screen getTargetScreen() {
        return targetScreen;
    }
}
