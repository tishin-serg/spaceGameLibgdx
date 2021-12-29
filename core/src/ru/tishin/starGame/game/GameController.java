package ru.tishin.starGame.game;

/*
в этом классе мы всё инициализируем, апдейтим.
Этот клас хранит и даёт доступ ко всем игровым ресурсам.
 */

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
        this.asteroidController = new AsteroidController();
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
        for (int i = 0; i < asteroidList.size(); i++) {
            Asteroid asteroid = asteroidList.get(i);
            for (int j = 0; j < bulletList.size(); j++) {
                Bullet bullet = bulletList.get(j);
                if (bullet.getPosition().dst(asteroid.getPosition()) < 32) {
                    bullet.deactivate();
                    asteroid.deactivate();
                }
            }
        }
    }

}
