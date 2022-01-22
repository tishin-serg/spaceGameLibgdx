package ru.tishin.starGame.game;

/*
в этом классе мы всё инициализируем, апдейтим.
Этот клас хранит и даёт доступ ко всем игровым ресурсам.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import ru.tishin.starGame.screen.ScreenManager;

import java.util.List;

public class GameController {
    private Background background;
    private BulletController bulletController;
    private AsteroidController asteroidController;
    private BonusController bonusController;
    private Hero hero;
    private Vector2 tempVector;
    private ParticleController particleController;
    private boolean isPause;

    public GameController() {
        this.background = new Background(this);
        this.hero = new Hero(this);
        this.bulletController = new BulletController(this);
        this.asteroidController = new AsteroidController(this);
        this.tempVector = new Vector2();
        this.particleController = new ParticleController();
        this.bonusController = new BonusController(this);
        this.isPause = false;

        for (int i = 0; i < 5; i++) {
            asteroidController.setup(MathUtils.random(0, ScreenManager.SCREEN_WIDTH),
                    MathUtils.random(0, ScreenManager.SCREEN_HEIGHT),
                    MathUtils.random(-200f, 200f),
                    MathUtils.random(-200f, 200f), 1.0f);
        }
    }

    public void pause() {
        if (isPause) isPause = false;
        else isPause = true;
    }

    public boolean isPause() {
        return isPause;
    }

    public BonusController getBonusController() {
        return bonusController;
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

    public ParticleController getParticleController() {
        return particleController;
    }

    public void update(float dt) {
        checkPressedKeys();

        if (!isPause) {
            background.update(dt);
            hero.update(dt);
            asteroidController.update(dt);
            bulletController.update(dt);
            particleController.update(dt);
            bonusController.update(dt);
            checkCollisions();
        }

    }

    private void checkPressedKeys() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            pause();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
        }
    }

    private void checkCollisions() {
        List<Asteroid> asteroidList = asteroidController.getActiveList();
        List<Bonus> bonusList = bonusController.getActiveList();

        for (int i = 0; i < asteroidList.size(); i++) {
            Asteroid asteroid = asteroidList.get(i);
            if (asteroid.getHitArea().overlaps(hero.getHitArea())) {
                // расстояние между центрами
                float dst = asteroid.getPosition().dst(hero.getPosition());
                // вычисляем половину расстояния, на которое требуется сдвинуть объекты
                float halfOverLen = (asteroid.getHitArea().radius + hero.getHitArea().radius - dst) / 2;
                // получаем вектор направления от астероида к герою и нормируем его
                tempVector.set(hero.getPosition()).sub(asteroid.getPosition()).nor();
                hero.getPosition().mulAdd(tempVector, halfOverLen);
                asteroid.getPosition().mulAdd(tempVector, -halfOverLen);
                // сумма радиусов, для определения коэф импульса
                float sumScl = hero.getHitArea().radius + asteroid.getHitArea().radius;
                hero.getVelocity().mulAdd(tempVector, asteroid.getHitArea().radius / sumScl * 100);
                asteroid.getVelocity().mulAdd(tempVector, -hero.getHitArea().radius / sumScl * 100);
                if (asteroid.takeDamage(2)) {
                    hero.changeScore(asteroid.getHpMax() * 50);
                }
                hero.takeDamage(asteroid.getHpMax() / 2);
                break;
            }
        }

        for (int i = 0; i < bonusList.size(); i++) {
            Bonus bonus = bonusList.get(i);
            if (hero.getHitArea().overlaps(bonus.getHitArea())) {
                hero.takeBonus(bonus);
                particleController.takeBonusEffect(bonus.getPosition().x, bonus.getPosition().y);
            }
        }

        List<Bullet> bulletList = bulletController.getActiveList();
        for (int i = 0; i < bulletList.size(); i++) {
            Bullet bullet = bulletList.get(i);
            for (int j = 0; j < asteroidList.size(); j++) {
                Asteroid asteroid = asteroidList.get(j);
                if (asteroid.getHitArea().contains(bullet.getPosition())) {
                    particleController.setup(bullet.getPosition().x + MathUtils.random(-4, 4),
                            bullet.getPosition().y + MathUtils.random(-4, 4),
                            bullet.getVelocity().x * -0.3f + MathUtils.random(-30, 30),
                            bullet.getVelocity().y * -0.3f + MathUtils.random(-30, 30),
                            0.2f, 2.2f, 1.5f,
                            1f, 1f, 1f, 1,
                            0, 0, 1, 0);


                    bullet.deactivate();
                    if (asteroid.takeDamage(hero.getCurrentWeapon().getDamage())) {
                        hero.changeScore(asteroid.getHpMax() * 100);
                        for (int k = 0; k < 3; k++) {
                            bonusController.setup(asteroid.getPosition().x, asteroid.getPosition().y,
                                    asteroid.getScale() * 0.25f);
                        }
                    }
                    break;
                }
            }
        }


    }

    public void saveScore(int score) {
        ScreenManager.getInstance().saveFinishScore(score);
    }

    public void dispose() {
        background.dispose();
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
