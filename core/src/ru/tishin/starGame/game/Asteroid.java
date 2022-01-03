package ru.tishin.starGame.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import ru.tishin.starGame.game.helpers.Poolable;
import ru.tishin.starGame.screen.ScreenManager;
import ru.tishin.starGame.screen.utils.Assets;

public class Asteroid implements Poolable {
    private final float BASE_SIZE = 64;
    private final float BASE_RADIUS = BASE_SIZE / 2;
    private GameController gameController;
    private TextureRegion texture;
    private Vector2 position;
    private Vector2 velocity;
    private float rotation;
    private float angle;
    private float scale;
    private boolean active;
    private int hp;
    private int hpMax;
    private Circle hitArea;


    public Asteroid(GameController gameController) {
        this.texture = Assets.getInstance().getAtlas().findRegion("asteroid");
        this.gameController = gameController;
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.hitArea = new Circle(0, 0, 0);
        this.active = false;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getRotation() {
        return rotation;
    }

    public Circle getHitArea() {
        return hitArea;
    }

    public int getHp() {
        return hp;
    }

    public int getHpMax() {
        return hpMax;
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt); // позиция меняется в зависимости от скорости и дельты
        angle += rotation * dt;
//        rotation += 100.0f * dt;
//        velocity.x = MathUtils.cosDeg(direction) * 2000.0f * dt;
//        velocity.y = MathUtils.sinDeg(direction) * 2000.0f * dt;
//        if (position.x <= -20 || position.y <= -20 || position.x >= ScreenManager.SCREEN_WIDTH + 20
//                || position.y >= ScreenManager.SCREEN_HEIGHT + 20) {
//            deactivate();
//        }

        if (position.x < -BASE_SIZE) {
            position.x = ScreenManager.SCREEN_WIDTH + BASE_SIZE;
        }
        if (position.y < -BASE_SIZE) {
            position.y = ScreenManager.SCREEN_HEIGHT + BASE_SIZE;
        }
        if (position.x > ScreenManager.SCREEN_WIDTH + BASE_SIZE) {
            position.x = -BASE_SIZE;
        }
        if (position.y > ScreenManager.SCREEN_HEIGHT + BASE_SIZE) {
            position.y = -BASE_SIZE;
        }
        hitArea.setPosition(position);
    }

    public boolean takeDamage(int amount) {
        hp -= amount;
        if (hp <= 0) {
            deactivate();
            if (scale > 0.5f)
                for (int i = 0; i < 3; i++) {
                    gameController.getAsteroidController().setup(position.x, position.y, MathUtils.random(-150, 150),
                            MathUtils.random(-150, 150), scale - 0.25f);
                }
            return true;
        } else {
            return false;
        }
    }

    public void activate(float x, float y, float vx, float vy, float scale) {
        position.set(x, y);
        velocity.set(vx, vy);
        active = true;
        hpMax = (int) (10 * scale);
        hp = hpMax;
        angle = MathUtils.random(0f, 360f);
        rotation = MathUtils.random(-180f, 180f);
        this.scale = scale;
        hitArea.setPosition(position);
        hitArea.setRadius(BASE_SIZE * this.scale * 0.8f);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, getPosition().x - 32, getPosition().y - 32, 32, 32, 64, 64, scale,
                scale, angle);
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
