package ru.tishin.starGame.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import ru.tishin.starGame.StarGame;
import ru.tishin.starGame.screen.ScreenManager;

public class Background {
    private final int STAR_COUNT = 1000;
    private Texture textureSpace;
    private Texture textureStar;
    private Star[] stars;
    private StarGame game;
    private GameController controller;

    public Background(GameController controller) {
        this.controller = controller;
        this.textureSpace = new Texture("bg.jpeg");
        this.textureStar = new Texture("star16.png");
        this.stars = new Star[STAR_COUNT];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star();
        }
    }

    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(textureSpace, 0, 0);
        for (int i = 0; i < stars.length; i++) {
            spriteBatch.draw(textureStar,
                    stars[i].position.x - 8, stars[i].position.y - 8, 8, 8, 16, 16, stars[i].scale, stars[i].scale, 0, 0, 0,
                    16, 16, false, false);
            if (MathUtils.random(0, 300) < 1) {
                spriteBatch.draw(textureStar,
                        stars[i].position.x - 8, stars[i].position.y - 8, 8, 8, 16, 16, stars[i].scale * 2, stars[i].scale * 2,
                        0, 0, 0,
                        16, 16, false, false);
            }
        }
    }

    public void update(float dt) {
        for (int i = 0; i < stars.length; i++) {
            stars[i].update(dt);
        }
    }

    private class Star {
        Vector2 position;
        Vector2 velocity;
        float scale;

        public Star() {
            this.position = new Vector2(MathUtils.random(-200, ScreenManager.SCREEN_WIDTH + 200),
                    MathUtils.random(-200, ScreenManager.SCREEN_HEIGHT + 200));
            this.velocity = new Vector2(MathUtils.random(-40, -5), 0);
            this.scale = Math.abs(velocity.x) / 40f; // размер зависит от скорости
        }

        public void update(float dt) {
            position.x += (velocity.x - controller.getHero().getVelocity().x * 0.1) * dt;
            position.y += (velocity.y - controller.getHero().getVelocity().y * 0.1) * dt;
            if (position.x < -200) {
                position.x = ScreenManager.SCREEN_WIDTH + 200;
                position.y = MathUtils.random(-200, ScreenManager.SCREEN_HEIGHT + 200);
            }
        }
    }
}
