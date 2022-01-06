package ru.tishin.starGame.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.tishin.starGame.game.helpers.ObjectPool;

public class BonusController extends ObjectPool<Bonus> {
    private GameController gameController;

    public BonusController(GameController gameController) {
        this.gameController = gameController;
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            Bonus b = activeList.get(i);
            b.render(batch);
        }
    }

    public void setup(float x, float y) {
        Bonus b = getActiveElement();
        b.activate(x, y);
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
