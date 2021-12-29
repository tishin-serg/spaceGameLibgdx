package ru.tishin.starGame.game;

import com.badlogic.gdx.math.Vector2;
import ru.tishin.starGame.game.helpers.Poolable;
import ru.tishin.starGame.screen.ScreenManager;

public class Bullet implements Poolable {
    private boolean active;
    private Vector2 position;
    private Vector2 velocity;

    public Bullet() {
        this.active = false;
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void deactivate() {
        active = false;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt); // позиция меняется в зависимости от скорости и дельты
        if (position.x <= -20 || position.y <= -20 || position.x >= ScreenManager.SCREEN_WIDTH + 20
                || position.y >= ScreenManager.SCREEN_HEIGHT + 20) {
            deactivate();
        }
    }

    public void activate(float x, float y, float vx, float vy) {
        position.set(x, y);
        velocity.set(vx, vy);
        active = true;
    }
}
