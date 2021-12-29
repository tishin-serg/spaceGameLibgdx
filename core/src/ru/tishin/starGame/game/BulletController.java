package ru.tishin.starGame.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.tishin.starGame.game.helpers.ObjectPool;

public class BulletController extends ObjectPool<Bullet> {
    private Texture texture;

    public BulletController() {
        this.texture = new Texture("bullet.png");
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            Bullet b = activeList.get(i);
            // Рисуем пулю. Отнимаем половину от координат, чтобы пуля отрисовывалась с центра.
            batch.draw(texture, b.getPosition().x - 16, b.getPosition().y - 16);
        }
    }

    // метод активирует пулю
    public void setup(float x, float y, float vx, float vy) {
        getActiveElement().activate(x, y, vx, vy);
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        // если появились неактивные пули, то переводим их в другой лист
        checkPool();
    }

    @Override
    protected Bullet newObject() {
        return new Bullet();
    }
}
