package ru.tishin.starGame.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.tishin.starGame.game.helpers.ObjectPool;

public class BotController extends ObjectPool<Bot> {
    private GameController gameController;

    public BotController(GameController gameController) {
        this.gameController = gameController;
    }

    public void render(SpriteBatch spriteBatch) {
        for (int i = 0; i < activeList.size(); i++) {
            Bot b = activeList.get(i);
            b.render(spriteBatch);
        }
    }

    public void setup(float x, float y, float vx, float vy) {
        Bot b = getActiveElement();
        b.activate(x, y, vx, vy);
    }

    @Override
    protected Bot newObject() {
        return new Bot(gameController);
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }
}
