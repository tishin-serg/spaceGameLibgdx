package ru.tishin.starGame.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import ru.tishin.starGame.game.helpers.ObjectPool;
import ru.tishin.starGame.screen.ScreenManager;

public class AsteroidController extends ObjectPool<Asteroid> {
    private Texture texture;
    private float timer;

    public AsteroidController() {
        this.texture = new Texture("asteroid.png");
    }

    public void render(SpriteBatch spriteBatch) {
        for (int i = 0; i < activeList.size(); i++) {
            Asteroid a = activeList.get(i);
            spriteBatch.draw(texture, a.getPosition().x - 32, a.getPosition().y - 32, 32, 32, 64, 64, 1,
                    1, a.getRotation(), 0, 0, 64, 64, false, false);
        }
    }

    public void setup(float x, float y) {
        Asteroid a = getActiveElement();
        a.activate(x, y);
    }

    /*
    Метод инициализирует астероиды раз в две секунды
     */
    public void init() {
        if (timer > MathUtils.random(1f, 5f)) {
            setup(ScreenManager.SCREEN_WIDTH, MathUtils.random(ScreenManager.SCREEN_HEIGHT));
            timer = 0f;
        }
    }


    public void update(float dt) {
        timer += dt;
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        if (activeList.size() < 10) init();
        // если появились неактивные астероиды, то переводим их в другой лист
        checkPool();
    }

    @Override
    protected Asteroid newObject() {
        return new Asteroid();
    }
}
