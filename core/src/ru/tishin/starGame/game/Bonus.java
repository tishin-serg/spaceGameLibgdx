package ru.tishin.starGame.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import ru.tishin.starGame.game.helpers.Poolable;

public class Bonus implements Poolable {
    private boolean active;
    private Vector2 position;
    private Vector2 velocity;
    private float lifeTime;
    private float rotation;
    private BonusType bonusType;
    private GameController gameController;
    private Circle hitArea;
    private int value;

    public Bonus(GameController gameController) {
        this.active = false;
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.lifeTime = 0f;
        this.rotation = 0f;
        this.gameController = gameController;
        this.hitArea = new Circle(0, 0, 0);
    }

    public Circle getHitArea() {
        return hitArea;
    }

    public void activate(BonusType bonusType, float x, float y, int value) {
        this.bonusType = bonusType;
        position.set(x, y);
        hitArea.set(x, y,  30f);
        velocity.set(MathUtils.random(-1f, 1f), MathUtils.random(-1f, 1f));
        velocity.nor().scl(50f);
        this.value = value;
        active = true;
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        hitArea.set(position.x, position.y, 30f);
        rotation += dt * 20;
        lifeTime += dt;
        if (lifeTime >= 7f) {
            deactivate();
        }
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public float getLifeTime() {
        return lifeTime;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        active = false;
    }

    public BonusType getBonusType() {
        return bonusType;
    }

    public enum BonusType {
        HP(0),
        AMMO(1),
        COINS(2);

        private int index;

        BonusType(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }
}
