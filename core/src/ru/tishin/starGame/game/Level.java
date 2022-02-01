package ru.tishin.starGame.game;

import com.badlogic.gdx.math.MathUtils;
import ru.tishin.starGame.screen.ScreenManager;

public class Level {
    private GameController gameController;
    private int currentLevel;

    public Level(GameController gameController) {
        this.gameController = gameController;
        currentLevel = 1;
    }

    public void initAsteroids() {
        for (int i = 0; i < 1; i++) {
            gameController.getAsteroidController().setup(MathUtils.random(0, ScreenManager.SCREEN_WIDTH),
                    MathUtils.random(0, ScreenManager.SCREEN_HEIGHT),
                    MathUtils.random(-200f, 200f),
                    MathUtils.random(-200f, 200f), 1.0f);
        }
    }

    public void increaseLevel() {
        currentLevel++;
        gameController.getBotController().setup(MathUtils.random(0, ScreenManager.SCREEN_WIDTH),
                MathUtils.random(0, ScreenManager.SCREEN_HEIGHT),
                0,
                0);
        // initAsteroids();
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

}
