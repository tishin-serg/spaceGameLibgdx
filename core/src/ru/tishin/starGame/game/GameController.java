package ru.tishin.starGame.game;

/*
в этом классе мы всё инициализируем, апдейтим.
Этот клас хранит и даёт доступ ко всем игровым ресурсам.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.StringBuilder;
import ru.tishin.starGame.screen.ScreenManager;

import java.util.List;

public class GameController {
    private Background background;
    private BulletController bulletController;
    private AsteroidController asteroidController;
    private BonusController bonusController;
    private BotController botController;
    private Hero hero;
    private Vector2 tempVector;
    private Vector2 tempVector2;
    private ParticleController particleController;
    private Stage stage;
    private boolean isPause;
    private Level level;
    private float timer;
    private InfoController infoController;
    private StringBuilder sb;

    public GameController(SpriteBatch batch) {
        this.background = new Background(this);
        this.hero = new Hero(this);
        this.bulletController = new BulletController(this);
        this.asteroidController = new AsteroidController(this);
        this.botController = new BotController(this);
        this.level = new Level(this);
        this.tempVector = new Vector2();
        this.tempVector2 = new Vector2();
        this.particleController = new ParticleController();
        this.bonusController = new BonusController(this);
        this.infoController = new InfoController();
        this.isPause = false;
        this.stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        this.sb = new StringBuilder();
        stage.addActor(hero.getShop());
        Gdx.input.setInputProcessor(stage);
        level.initAsteroids();
    }

    public void turnPause() {
        isPause = !isPause;

        if (isPause) {
            ScreenManager.getInstance().getTargetScreen().pause();
        } else {
            ScreenManager.getInstance().getTargetScreen().resume();
        }
    }

    public void update(float dt) {
        checkPressedKeys();
        if (isPause) return;
        timer += dt;
        background.update(dt);
        hero.update(dt);
        asteroidController.update(dt);
        bulletController.update(dt);
        particleController.update(dt);
        bonusController.update(dt);
        infoController.update(dt);
        botController.update(dt);
        checkCollisions();
        if (!hero.checkLive()) {
            saveHero(hero);
            ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME_OVER);
        }
        stage.act(dt);
    }

    private void checkPressedKeys() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            turnPause();
            hero.getShop().changeVisible();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
        }
    }

    private void checkCollisions() {
        List<Asteroid> asteroidList = asteroidController.getActiveList();
        List<Bonus> bonusList = bonusController.getActiveList();
        List<Bullet> bulletList = bulletController.getActiveList();
        List<Bot> botList = botController.getActiveList();
        checkAsteroidAndShipCollisions(asteroidList);
        checkGettingBonus(bonusList);
        checkHitsAsteroid(asteroidList, bulletList);
        checkHitsBot(botList, bulletList);
        checkHitsHero(bulletList);
    }

    private void checkHitsHero(List<Bullet> bulletList) {
        for (int i = 0; i < bulletList.size(); i++) {
            Bullet bullet = bulletList.get(i);
            if (hero.getHitArea().contains(bullet.getPosition())) {
                particleController.setup(bullet.getPosition().x + MathUtils.random(-4, 4),
                        bullet.getPosition().y + MathUtils.random(-4, 4),
                        bullet.getVelocity().x * -0.3f + MathUtils.random(-30, 30),
                        bullet.getVelocity().y * -0.3f + MathUtils.random(-30, 30),
                        0.2f, 2.2f, 1.5f,
                        1f, 1f, 1f, 1,
                        0, 0, 1, 0);
                bullet.deactivate();
                hero.takeDamage(3);
                break;
            }
        }
    }

    private void checkHitsBot(List<Bot> botList, List<Bullet> bulletList) {
        for (int i = 0; i < botList.size(); i++) {
            Bot bot = botList.get(i);
            for (int j = 0; j < bulletList.size(); j++) {
                Bullet bullet = bulletList.get(j);
                if (bot.getHitArea().contains(bullet.getPosition())) {
                    particleController.setup(bullet.getPosition().x + MathUtils.random(-4, 4),
                            bullet.getPosition().y + MathUtils.random(-4, 4),
                            bullet.getVelocity().x * -0.3f + MathUtils.random(-30, 30),
                            bullet.getVelocity().y * -0.3f + MathUtils.random(-30, 30),
                            0.2f, 2.2f, 1.5f,
                            1f, 1f, 1f, 1,
                            0, 0, 1, 0);
                    bullet.deactivate();
                    checkBotDestroy(bot);
                    break;
                }
            }
        }
    }

    private void checkAsteroidAndShipCollisions(List<Asteroid> asteroidList) {
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
                    if (asteroidController.getActiveList().size() == 1) {
                        level.increaseLevel();
                        timer = 0f;
                    }
                }
                hero.takeDamage(asteroid.getHpMax() / 2);
                sb.clear();
                sb.append("HP -").append(asteroid.getHpMax() / 2);
                infoController.setup(hero.position.x, hero.position.y, sb, Color.RED);
                break;
            }
        }
    }

    private void checkGettingBonus(List<Bonus> bonusList) {
        for (int i = 0; i < bonusList.size(); i++) {
            Bonus bonus = bonusList.get(i);

            if (hero.getAttractionArea().overlaps(bonus.getHitArea())) {
                tempVector2.set(hero.getPosition()).sub(bonus.getPosition()).nor();
                // bonus.getPosition().mulAdd(tempVector2, 1f);
                bonus.getVelocity().mulAdd(tempVector2, 30f);
            }

            if (hero.getHitArea().overlaps(bonus.getHitArea())) {
                hero.takeBonus(bonus);
                particleController.takeBonusEffect(bonus.getPosition().x, bonus.getPosition().y, bonus.getBonusType());
            }
        }
    }

    private void checkHitsAsteroid(List<Asteroid> asteroidList, List<Bullet> bulletList) {
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
                    checkAsteroidDestroy(asteroid);
                    break;
                }
            }
        }
    }

    private void checkAsteroidDestroy(Asteroid asteroid) {
        if (asteroid.takeDamage(hero.getCurrentWeapon().getDamage())) {
            hero.changeScore(asteroid.getHpMax() * 100);
            for (int k = 0; k < 3; k++) {
                bonusController.setup(asteroid.getPosition().x, asteroid.getPosition().y,
                        asteroid.getScale() * 0.25f);
            }
            if (asteroidController.getActiveList().size() == 1) {
                level.increaseLevel();
                timer = 0;
            }
        }
    }

    private void checkBotDestroy(Bot bot) {
        if (bot.takeDamage(hero.getCurrentWeapon().getDamage())) {
            hero.changeScore(bot.getHpMax() * 100);

            if (botController.getActiveList().size() == 0) {
//                level.increaseLevel();
//                timer = 0;
            }
        }
    }

    public void saveHero(Hero hero) {
        ScreenManager.getInstance().saveDeadHero(hero);
    }

    public void dispose() {
        background.dispose();
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

    public Stage getStage() {
        return stage;
    }

    public Level getLevel() {
        return level;
    }

    public float getTimer() {
        return timer;
    }

    public InfoController getInfoController() {
        return infoController;
    }

    public BotController getBotController() {
        return botController;
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
