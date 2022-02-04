package ru.tishin.starGame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.StringBuilder;
import ru.tishin.starGame.screen.ScreenManager;
import ru.tishin.starGame.screen.utils.Assets;

public class Hero {
    public enum Skill {
        HP_MAX(20, 10), HP(20, 10), WEAPON(100);
        int cost;
        int value;

        Skill(int cost) {
            this.cost = cost;
        }

        Skill(int cost, int value) {
            this.cost = cost;
            this.value = value;
        }
    }

    private final float SPEED = 500f;
    private GameController gameController;
    private TextureRegion texture;
    private Vector2 position;
    private Vector2 velocity;
    private Vector2 direction;
    private Circle hitArea;
    private Weapon currentWeapon;
    private int weaponNum;
    private StringBuilder stringBuilder;
    private float angle;
    private int score;
    private int scoreView;
    private int hp;
    private int hpMax;
    private boolean isLive;
    private int coins;
    private Shop shop;
    private Weapon[] weapons;

    public Hero(GameController gameController) {
        this.gameController = gameController;
        this.texture = Assets.getInstance().getAtlas().findRegion("ship");
        this.position = new Vector2(ScreenManager.SCREEN_WIDTH / 2, ScreenManager.SCREEN_HEIGHT / 2);
        this.velocity = new Vector2(0, 0);
        this.angle = 0.0f;
        this.direction = new Vector2(0, 0);
        this.hitArea = new Circle(position, 20);
        this.hpMax = 100;
        this.hp = hpMax;
        this.isLive = true;
        this.stringBuilder = new StringBuilder();
        this.coins = 100;
        this.shop = new Shop(this);
        createWeapons();
        this.weaponNum = 0;
        this.currentWeapon = weapons[weaponNum];
    }

    private void createWeapons() {
        this.weapons = new Weapon[]{
                new Weapon(gameController, this, "Laser", 0, 1, 500f, 200, 0.2f,
                        new Vector3[]{
                                new Vector3(28, 0, 0)
                        }),
                new Weapon(gameController, this, "Laser", 0, 1, 400f, 200, 0.3f,
                        new Vector3[]{
                                new Vector3(28, -90, 0),
                                new Vector3(28, 90, 0)
                        }),
                new Weapon(gameController, this, "Laser", 0, 1, 400f, 200, 0.3f,
                        new Vector3[]{
                                new Vector3(28, 0, 0),
                                new Vector3(28, 90, 20),
                                new Vector3(28, -90, -20),
                        }),
                new Weapon(gameController, this, "Laser", 0, 1, 600f, 400, 0.2f,
                        new Vector3[]{
                                new Vector3(28, 0, 0),
                                new Vector3(28, 90, 20),
                                new Vector3(28, -90, -20),
                        }),
                new Weapon(gameController, this, "Laser", 0, 2, 600f, 500, 0.2f,
                        new Vector3[]{
                                new Vector3(28, 0, 0),
                                new Vector3(28, 90, 20),
                                new Vector3(28, -90, -20),

                        })
        };
    }

    public void changeScore(int delta) {
        score += delta;
    }

    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(texture, position.x - 32, position.y - 32, 32, 32, 64, 64,
                1, 1, angle);
    }

    public boolean takeDamage(int amount) {
        hp -= amount;
        return !checkLive();
    }

    public void increaseHp(int hp) {
        this.hp += hp;
        if (this.hp > hpMax) this.hp = hpMax;
    }

    public boolean checkLive() {
        return hp > 0;
    }

    public boolean upgradeFromStore(Skill skill) {
        switch (skill) {
            case HP_MAX:
                hpMax += Skill.HP_MAX.value;
                return true;
            case HP:
                increaseHp(Skill.HP.value);
                return true;
            case WEAPON:
                if (weaponNum < weapons.length - 1) {
                    weaponNum++;
                    currentWeapon = weapons[weaponNum];
                    return true;
                }
        }
        return false;
    }

    public void update(float dt) {
        currentWeapon.update(dt);
        checkHeroScore(dt);
        checkPressedKeys(dt);

        /*
        Меняем позицию корабля. Прибавляем к вектору позиции объекта вектор ускорения.
        Умножаем на скаляр (чтобы скорость не зависела от частоты фпс).
         */
        position.mulAdd(velocity, dt);
        hitArea.setPosition(position);
        direction.set(MathUtils.cosDeg(angle), MathUtils.sinDeg(angle));

        slowingDown(dt);
        checkGameBounds();
    }

    public boolean isMoneyEnough(int amount) {
        return amount <= coins;
    }

    public void decreaseMoney(int amount) {
        if (isMoneyEnough(amount)) {
            coins -= amount;
        }
    }

    private void checkPressedKeys(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            shooting();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            angle += 180.0f * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            angle -= 180.0f * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            velocity.add(MathUtils.cosDeg(angle) * SPEED * dt, MathUtils.sinDeg(angle) * SPEED * dt);
            float bx = position.x + MathUtils.cosDeg(angle + 180) * 30;
            float by = position.y + MathUtils.sinDeg(angle + 180) * 30;
            for (int i = 0; i < 3; i++) {
                gameController.getParticleController().setup(bx + MathUtils.random(-4, 4), by + MathUtils.random(-4, 4),
                        velocity.x * -0.1f + MathUtils.random(-20, 20), velocity.y * -0.1f + MathUtils.random(-20, 20),
                        0.3f, 1.2f, 0.2f,
                        1.0f, 0.5f, 0, 1,
                        1, 1, 1, 0);
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            velocity.add(MathUtils.cosDeg(angle) * (-SPEED / 2) * dt, MathUtils.sinDeg(angle) * (-SPEED / 2) * dt);
            float bx = position.x + MathUtils.cosDeg(angle + 90) * -25;
            float by = position.y + MathUtils.sinDeg(angle + 90) * -25;
            for (int i = 0; i < 2; i++) {
                gameController.getParticleController().setup(bx + MathUtils.random(-4, 4), by + MathUtils.random(-4, 4),
                        velocity.x * 0.1f + MathUtils.random(-20, 20), velocity.y * 0.1f + MathUtils.random(-20, 20),
                        0.3f, 1.2f, 0.2f,
                        1f, 0.5f, 0f, 1f,
                        1f, 1f, 1f, 0);
            }
            bx = position.x + MathUtils.cosDeg(angle - 90) * -25;
            by = position.y + MathUtils.sinDeg(angle - 90) * -25;
            for (int i = 0; i < 2; i++) {
                gameController.getParticleController().setup(bx + MathUtils.random(-4, 4), by + MathUtils.random(-4, 4),
                        velocity.x * 0.1f + MathUtils.random(-20, 20), velocity.y * 0.1f + MathUtils.random(-20, 20),
                        0.3f, 1.2f, 0.2f,
                        1f, 0.5f, 0f, 1f,
                        1f, 1f, 1f, 0);
            }
        }
    }

    private void checkHeroScore(float dt) {
        if (score > scoreView) {
            scoreView += 1000 * dt;
            if (scoreView > score) {
                scoreView = score;
            }
        }
    }

    public void shooting() {
        currentWeapon.fire();
    }

    public void slowingDown(float dt) {
        float stopCf = 1.0f - 1.0f * dt;
        if (stopCf < 0.0f) {
            stopCf = 0.0f;
        }
        velocity.scl(stopCf);
    }

    public void checkGameBounds() {
        if (position.x < 32) {
            position.x = 32;
            velocity.x *= -0.5f;
        }
        if (position.y < 32) {
            position.y = 32;
            velocity.y *= -0.5f;
        }
        if (position.x > ScreenManager.SCREEN_WIDTH - 32) {
            position.x = ScreenManager.SCREEN_WIDTH - 32;
            velocity.x *= -0.5f;
        }
        if (position.y > ScreenManager.SCREEN_HEIGHT - 32) {
            position.y = ScreenManager.SCREEN_HEIGHT - 32;
            velocity.y *= -0.5f;
        }
    }

    public void takeBonus(Bonus bonus) {
        Bonus.BonusType bonusType = bonus.getBonusType();
        int value = bonus.getValue();
        switch (bonusType) {
            case HP:
                increaseHp(value);
                bonus.deactivate();
                break;
            case AMMO:
                currentWeapon.takeBulletBonus(value);
                bonus.deactivate();
                break;
            case COINS:
                coins += value;
                bonus.deactivate();
                break;
            default:
                bonus.deactivate();
                System.out.println("Бонус не найден");
        }
    }

    public void renderGUI(SpriteBatch batch, BitmapFont font32) {
        stringBuilder.clear();
        stringBuilder.append("SCORE: ").append(getScoreView()).append("\n");
        stringBuilder.append("HP: ").append(hp).append(" / ").append(hpMax).append("\n");
        stringBuilder.append("AMMO: ").append(currentWeapon.getBulletCountCurrent()).append(" / ").append(currentWeapon.getBulletCountMax()).append("\n");
        stringBuilder.append("COINS: ").append(coins);
        font32.draw(batch, stringBuilder, 50, ScreenManager.SCREEN_HEIGHT - 50);
    }

    public float getAngle() {
        return angle;
    }

    public Weapon getCurrentWeapon() {
        return currentWeapon;
    }

    public int getCoins() {
        return coins;
    }

    public Shop getShop() {
        return shop;
    }

    public int getHpMax() {
        return hpMax;
    }

    public int getScore() {
        return score;
    }

    public int getHp() {
        return hp;
    }

    public int getScoreView() {
        return scoreView;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getDirection() {
        return direction;
    }

    public Circle getHitArea() {
        return hitArea;
    }


}
