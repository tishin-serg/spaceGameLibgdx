package ru.tishin.starGame.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import ru.tishin.starGame.game.helpers.Poolable;
import ru.tishin.starGame.screen.utils.Assets;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Bonus implements Poolable {
    private boolean active;
    private Vector2 position;
    private float lifeTime;
    private TextureRegion textureRegion;
    private float rotation;
    private BonusType bonusType;
    private GameController gameController;
    private Circle hitArea;

    public Bonus(GameController gameController) {
        this.active = false;
        this.position = new Vector2(0, 0);
        this.lifeTime = MathUtils.random(5, 10);
        this.textureRegion = Assets.getInstance().getAtlas().findRegion("bonus");
        this.rotation = 0f;
        this.gameController = gameController;
        this.hitArea = new Circle(0, 0, 0);
    }

    public Circle getHitArea() {
        return hitArea;
    }

    public void activate(float x, float y) {
        bonusType = BonusType.randomBonusType();
        position.set(x, y);
        hitArea.set(x, y, (float) textureRegion.getRegionWidth() / 2);
        active = true;
    }

    public void update(float dt) {
        rotation += dt * 20;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void render(SpriteBatch batch) {
        batch.draw(textureRegion, position.x - 32, position.y - 32, 32, 32, 64, 64, 1,
                1, rotation);
    }

    public void deactivate() {
        active = false;
    }

    public BonusType getBonusType() {
        return bonusType;
    }

    public enum BonusType {
        HP(50),
        BULLETS(100),
        COINS(10);

        private static final List<BonusType> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
        private static final int SIZE = VALUES.size();
        private int value;

        BonusType(int value) {
            this.value = value;
        }

        public static BonusType randomBonusType() {
            return VALUES.get(MathUtils.random(SIZE - 1));
        }

        public int getValue() {
            return value;
        }
    }
}
