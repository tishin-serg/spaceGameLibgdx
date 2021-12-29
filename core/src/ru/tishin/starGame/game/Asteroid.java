package ru.tishin.starGame.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import ru.tishin.starGame.game.helpers.Poolable;
import ru.tishin.starGame.screen.ScreenManager;

public class Asteroid implements Poolable {
    private Vector2 position;
    private Vector2 velocity;
    private float rotation;
    private float direction;
    private boolean active;

    public Asteroid() {
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.direction = MathUtils.random(170f, 190f);
        this.rotation = 0.0f;
        this.active = false;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getRotation() {
        return rotation;
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt); // позиция меняется в зависимости от скорости и дельты
        rotation += 100.0f * dt;
        velocity.x = MathUtils.cosDeg(direction) * 10000.0f * dt;
        velocity.y = MathUtils.sinDeg(direction) * 10000.0f * dt;
        if (position.x <= -20 || position.y <= -20 || position.x >= ScreenManager.SCREEN_WIDTH + 20
                || position.y >= ScreenManager.SCREEN_HEIGHT + 20) {
            deactivate();
        }
    }

    public void activate(float x, float y) {
        position.set(x, y);
        active = true;
    }

    public void deactivate() {
        active = false;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    /*
    public void update(float dt) {
        rotation += 100.f * dt;
        velocity.x = MathUtils.cosDeg(direction) * 20000.0f * dt;
        velocity.y = MathUtils.sinDeg(direction) * 20000.0f * dt;
//        position.x += (velocity.x - controller.getHero().getVelocity().x) * dt;
//        position.y += (velocity.y - controller.getHero().getVelocity().y) * dt;

        if (position.x < -32) {
            position.x = ScreenManager.SCREEN_WIDTH;
            direction += MathUtils.random(165, 195);
            System.out.println("лево");
        }
        if (position.y < -32) {
            position.y = ScreenManager.SCREEN_HEIGHT;
//            direction += MathUtils.random(255, 285);
//            direction += MathUtils.random(75, 105);
            System.out.println("низ");
        }
        if (position.x > ScreenManager.SCREEN_WIDTH + 32) {
            position.x = 32;
            System.out.println("право");
            direction += MathUtils.random(-15, 15);
        }
        if (position.y > ScreenManager.SCREEN_HEIGHT + 32) {
            position.y = 32;
            System.out.println("верх");
//            // direction += MathUtils.random(255, 285);
//             direction += MathUtils.random(75, 105);
        }
    }

     */
}
