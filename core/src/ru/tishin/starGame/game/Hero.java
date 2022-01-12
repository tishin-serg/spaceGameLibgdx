package ru.tishin.starGame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import ru.tishin.starGame.screen.ScreenManager;
import ru.tishin.starGame.screen.utils.Assets;

public class Hero {
    private final float SPEED = 500f;
    private final float RELOAD = 0.2f;
    private final int HP_MAX = 50;
    private GameController gameController;
    private TextureRegion texture;
    private Vector2 position;
    private Vector2 velocity;
    private Vector2 direction;
    private Circle hitArea;
    private float angle;
    private float fireTimer;
    private int score;
    private int scoreView;
    private int hp;
    private boolean isLive;

    public Hero(GameController gameController) {
        this.gameController = gameController;
        this.texture = Assets.getInstance().getAtlas().findRegion("ship");
        this.position = new Vector2(ScreenManager.SCREEN_WIDTH / 2, ScreenManager.SCREEN_HEIGHT / 2);
        this.velocity = new Vector2(0, 0);
        this.angle = 0.0f;
        this.direction = new Vector2(0, 0);
        this.hitArea = new Circle(0, 0, 0);
        this.hp = HP_MAX;
        this.isLive = true;
    }

    public int getScore() {
        return score;
    }

    public int getHp() {
        return hp;
    }

    public int getScoreView() {
        return scoreView;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getDirection() {
        return direction;
    }

    public Circle getHitArea() {
        return hitArea;
    }

    public void changeScore(int delta) {
        score += delta;
    }

    public void render(SpriteBatch spriteBatch) {
        if (isLive) {
            spriteBatch.draw(texture, position.x - 32, position.y - 32, 32, 32, 64, 64,
                    1, 1, angle);
        }
    }

    public boolean takeDamage(int amount) {
        hp -= amount;
        if (hp <= 0) {
            isLive = false;
            return true;
        } else {
            return false;
        }
    }

    public void update(float dt) {
        // С каждым кадром прибавляем к таймеру дельту времени.
        fireTimer += dt;
        hitArea.setPosition(position);

        if (score > scoreView) {
            scoreView += 1000 * dt;
            if (scoreView > score) {
                scoreView = score;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            if (fireTimer > RELOAD) {
                fireTimer = 0;
                float wx = position.x + MathUtils.cosDeg(angle + 90) * 20;
                float wy = position.y + MathUtils.sinDeg(angle + 90) * 20;
                gameController.getBulletController().setup(wx, wy,
                        MathUtils.cosDeg(angle) * 500f + velocity.x,
                        MathUtils.sinDeg(angle) * 500f + velocity.y);
                wx = position.x + MathUtils.cosDeg(angle - 90) * 20;
                wy = position.y + MathUtils.sinDeg(angle - 90) * 20;
                gameController.getBulletController().setup(wx, wy,
                        MathUtils.cosDeg(angle) * 500f + velocity.x,
                        MathUtils.sinDeg(angle) * 500f + velocity.y);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            angle += 180.0f * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            angle -= 180.0f * dt;
        }
        /*
        Пока нажимаем W, увеличивается ускорение.
         */
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
//            velocity.x += MathUtils.cosDeg(angle) * SPEED * dt;
//            velocity.y += MathUtils.sinDeg(angle) * SPEED * dt;
            velocity.add(MathUtils.cosDeg(angle) * SPEED * dt, MathUtils.sinDeg(angle) * SPEED * dt);
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
//            velocity.x += MathUtils.cosDeg(angle) * (-SPEED / 2) * dt;
//            velocity.y += MathUtils.sinDeg(angle) * (-SPEED / 2) * dt;
            velocity.add(MathUtils.cosDeg(angle) * (-SPEED / 2) * dt, MathUtils.sinDeg(angle) * (-SPEED / 2) * dt);
        }
//        position.x += velocity.x * dt;
//        position.y += velocity.y * dt;

        /*
        Меняем позицию корабля. Прибавляем к вектору позиции объекта вектор ускорения.
        Умножаем на скаляр (чтобы скорость не зависела от частоты фпс).
         */
        position.mulAdd(velocity, dt);

        // Замедление скорости
        float stopCf = 1.0f - 1.0f * dt;
        if (stopCf < 0.0f) {
            stopCf = 0.0f;
        }
        velocity.scl(stopCf);

        direction.set(MathUtils.cosDeg(angle), MathUtils.sinDeg(angle));

        if (position.x < 32) {
            position.x = 32;
            velocity.x *= -0.5f;
        }
        if (position.y < 32) {
            position.y = 32;
            velocity.y *= -0.5f;
        }
        if (position.x > ScreenManager.SCREEN_WIDTH - 32) {
            position.x = ScreenManager.SCREEN_WIDTH - 32;
            velocity.x *= -0.5f;
        }
        if (position.y > ScreenManager.SCREEN_HEIGHT - 32) {
            position.y = ScreenManager.SCREEN_HEIGHT - 32;
            velocity.y *= -0.5f;
        }
    }


}
