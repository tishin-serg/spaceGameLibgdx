package ru.tishin.starGame.game;

/*
в этом классе мы всё инициализируем, апдейтим.
Этот клас хранит и даёт доступ ко всем игровым ресурсам.
 */

import com.badlogic.gdx.math.MathUtils;
import ru.tishin.starGame.screen.ScreenManager;

import java.util.List;

public class GameController {
    private Background background;
    private BulletController bulletController;
    private AsteroidController asteroidController;
    private Hero hero;

    public GameController() {
        this.background = new Background(this);
        this.hero = new Hero(this);
        this.bulletController = new BulletController();
        this.asteroidController = new AsteroidController(this);

        for (int i = 0; i < 3; i++) {
            asteroidController.setup(MathUtils.random(0, ScreenManager.SCREEN_WIDTH),
                    MathUtils.random(0, ScreenManager.SCREEN_HEIGHT),
                    MathUtils.random(-200f, 200f),
                    MathUtils.random(-200f, 200f), 1.0f);
        }
    }

    public AsteroidController getAsteroidController() {
        return asteroidController;
    }

    public BulletController getBulletController() {
        return bulletController;
    }

    public Hero getHero() {
        return hero;
    }

    public Background getBackground() {
        return background;
    }


    public void update(float dt) {
        background.update(dt);
        hero.update(dt);
        asteroidController.update(dt);
        bulletController.update(dt);
        checkCollisions();
    }

    private void checkCollisions() {
        List<Asteroid> asteroidList = asteroidController.getActiveList();
        List<Bullet> bulletList = bulletController.getActiveList();
        for (int i = 0; i < bulletList.size(); i++) {
            Bullet bullet = bulletList.get(i);
            for (int j = 0; j < asteroidList.size(); j++) {
                Asteroid asteroid = asteroidList.get(j);
                if (asteroid.getHitArea().contains(bullet.getPosition())) {
                    bullet.deactivate();
                    if (asteroid.takeDamage(1)) {
                        hero.changeScore(asteroid.getHpMax() * 100);
                    }
                    break;
                }
            }
        }

        for (int i = 0; i < asteroidList.size(); i++) {
            Asteroid asteroid = asteroidList.get(i);
            if (asteroid.getHitArea().overlaps(hero.getHitArea())) {
                hero.takeDamage(asteroid.getHpMax() / 2);
                asteroid.takeDamage(asteroid.getHpMax() / 2);
                hero.getVelocity().x *= -1f;
                hero.getVelocity().y *= -1f;
                asteroid.getVelocity().x *= -1.5f;
                asteroid.getVelocity().y *= -1.5f;
                break;
            }
        }
    }

    /*public void checkSightDirection() {
        List<Asteroid> list = getAsteroidController().getActiveList();
        Vector2 hero = getHero().getPosition();
        for (int i = 0; i < list.size(); i++) {
            Vector2 asteroid = list.get(i).getPosition();
            // V
            Vector2 directionToAsteroid = new Vector2(asteroid.x - hero.x, asteroid.y - hero.y).nor();
            // D
            Vector2 directionHero = getHero().getDirection().nor();
            Vector2 vector2 = directionHero.scl(directionToAsteroid);
            float f = MathUtils.acos(vector2.x + vector2.y);
            if (MathUtils.isZero(f, 0.1f)) {
                System.out.println("Астероид " + list.get(i).toString() + " на прицеле");
            }
        }
    }*/
}
