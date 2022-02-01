package ru.tishin.starGame.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import ru.tishin.starGame.game.helpers.Poolable;
import ru.tishin.starGame.screen.ScreenManager;

public class Bullet implements Poolable {
    private boolean active;
    private Vector2 position;
    private Vector2 velocity;
    private GameController gameController;

    public Bullet(GameController gameController) {
        this.gameController = gameController;
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

        for (int i = 0; i < 2; i++) {
            gameController.getParticleController().setup(position.x + MathUtils.random(-4, 4), position.y + MathUtils.random(-4,
                    4),
                    velocity.x * -0.1f + MathUtils.random(-20, 20), velocity.y * -0.1f + MathUtils.random(-20, 20),
                    0.1f, 1.2f, 0.2f,
                    1.0f, 0.7f, 0, 1,
                    1, 1, 1, 0);
        }
    }

    public void activate(float x, float y, float vx, float vy) {
        position.set(x, y);
        velocity.set(vx, vy);
        active = true;
    }
}
