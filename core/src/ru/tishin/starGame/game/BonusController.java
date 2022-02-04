package ru.tishin.starGame.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import ru.tishin.starGame.game.helpers.ObjectPool;
import ru.tishin.starGame.screen.utils.Assets;

public class BonusController extends ObjectPool<Bonus> {
    private GameController gameController;
    private TextureRegion[][] textures;

    public BonusController(GameController gameController) {
        this.gameController = gameController;
        this.textures = new TextureRegion(Assets.getInstance().getAtlas().findRegion("powerups")).split(60, 60);
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            Bonus b = activeList.get(i);
            int frameIndex = (int) (b.getLifeTime() / 0.1f) % textures[b.getBonusType().getIndex()].length;
            batch.draw(textures[b.getBonusType().getIndex()][frameIndex], b.getPosition().x - 30, b.getPosition().y - 30);
        }
    }

    public void setup(float x, float y, float probability) {
        if (MathUtils.random() <= probability) {
            getActiveElement().activate(Bonus.BonusType.values()[MathUtils.random(0, Bonus.BonusType.values().length - 1)], x, y,
                    30);
        }
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }

    @Override
    protected Bonus newObject() {
        return new Bonus(gameController);
    }
}
