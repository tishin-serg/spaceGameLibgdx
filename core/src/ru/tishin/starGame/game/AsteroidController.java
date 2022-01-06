package ru.tishin.starGame.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.tishin.starGame.game.helpers.ObjectPool;

public class AsteroidController extends ObjectPool<Asteroid> {
    private GameController gameController;
    private float timer;

    public AsteroidController(GameController gameController) {
        this.gameController = gameController;
    }

    public void render(SpriteBatch spriteBatch) {
        for (int i = 0; i < activeList.size(); i++) {
            Asteroid a = activeList.get(i);
            a.render(spriteBatch);
        }
    }

    public void setup(float x, float y, float vx, float vy, float scale) {
        Asteroid a = getActiveElement();
        a.activate(x, y, vx, vy, scale);
    }

    /*
    Метод инициализирует астероиды раз в две секунды

    public void init() {
        if (timer > MathUtils.random(1f, 5f)) {
            setup(ScreenManager.SCREEN_WIDTH, MathUtils.random(ScreenManager.SCREEN_HEIGHT));
            timer = 0f;
        }
    }
     */


    public void update(float dt) {
        timer += dt;
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        // if (activeList.size() < 5) init();
        // если появились неактивные астероиды, то переводим их в другой лист
        checkPool();
    }

    @Override
    protected Asteroid newObject() {
        return new Asteroid(gameController);
    }
}
