package ru.tishin.starGame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.StringBuilder;
import ru.tishin.starGame.screen.ScreenManager;

public class Hero extends Ship {
    private StringBuilder sb;
    private int score;
    private int scoreView;
    private int coins;
    private Shop shop;
    private Circle attractionArea;

    public Hero(GameController gameController) {
        super(gameController, 100, 500f);
        position = new Vector2(ScreenManager.SCREEN_WIDTH / 2, ScreenManager.SCREEN_HEIGHT / 2);
        velocity = new Vector2(0, 0);
        sb = new StringBuilder();
        coins = 1000;
        shop = new Shop(this);
        attractionArea = new Circle(position, 0f);
        hitArea = new Circle(position, 20);
    }


    public void changeScore(int delta) {
        score += delta;
    }

    public int increaseHp(int hp) {
        int diffHp = hp;
        this.hp += hp;
        if (this.hp > hpMax) {
            diffHp = hp - (this.hp - hpMax);
            this.hp = hpMax;
        }
        return diffHp;
    }

    public boolean upgradeFromStore(Skill skill) {
//        sb.clear();
//        sb.append(skill.toString()).append(" +");
//        sb.append(skill.value).append("\n");
//        sb.append("COINS -").append(skill.cost);
//        gameController.getInfoController().setup(shop.getRight(), shop.getY(), sb, Color.GREEN);
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
            case ATTRACT:
                if (attractionArea.radius < 300) {
                    attractionArea.radius += 20;
                    return true;
                }
        }
        return false;
    }

    public void update(float dt) {
        super.update(dt);
        checkHeroScore(dt);
        checkPressedKeys(dt);
        attractionArea.setPosition(position);
    }

    public boolean isMoneyEnough(int amount) {
        return amount <= coins;
    }

    public void decreaseMoney(int amount) {
        // todo вынести сюда надписи
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
            velocity.add(MathUtils.cosDeg(angle) * engineSpeed * dt, MathUtils.sinDeg(angle) * engineSpeed * dt);
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
            velocity.add(MathUtils.cosDeg(angle) * (-engineSpeed / 2) * dt, MathUtils.sinDeg(angle) * (-engineSpeed / 2) * dt);
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
            scoreView += 3000 * dt;
            if (scoreView > score) {
                scoreView = score;
            }
        }
    }

    public void takeBonus(Bonus bonus) {
        Bonus.BonusType bonusType = bonus.getBonusType();
        int value = bonus.getValue();
        sb.clear();
        sb.append(bonus.getBonusType().toString()).append(" +");

        switch (bonusType) {
            case HP:
                sb.append(increaseHp(value));
                gameController.getInfoController().setup(bonus.getPosition().x, bonus.getPosition().y, sb, Color.GREEN);
                bonus.deactivate();
                break;
            case AMMO:
                sb.append(currentWeapon.takeBulletBonus(value));
                gameController.getInfoController().setup(bonus.getPosition().x, bonus.getPosition().y, sb, Color.BROWN);
                bonus.deactivate();
                break;
            case COINS:
                coins += value;
                sb.append(value);
                gameController.getInfoController().setup(bonus.getPosition().x, bonus.getPosition().y, sb, Color.YELLOW);
                bonus.deactivate();
                break;
            default:
                bonus.deactivate();
                System.out.println("Бонус не найден");
        }
    }

    public void renderGUI(SpriteBatch batch, BitmapFont font32) {
        sb.clear();
        sb.append("SCORE: ").append(getScoreView()).append("\n");
        sb.append("HP: ").append(hp).append(" / ").append(hpMax).append("\n");
        sb.append("AMMO: ").append(currentWeapon.getBulletCountCurrent()).append(" / ").append(currentWeapon.getBulletCountMax()).append("\n");
        sb.append("COINS: ").append(coins).append("\n");
        font32.draw(batch, sb, 50, ScreenManager.SCREEN_HEIGHT - 50);
    }

    public int getCoins() {
        return coins;
    }

    public Shop getShop() {
        return shop;
    }

    public int getScore() {
        return score;
    }

    public int getScoreView() {
        return scoreView;
    }

    public Circle getAttractionArea() {
        return attractionArea;
    }

    public enum Skill {
        HP_MAX(20, 10), HP(20, 10), WEAPON(100), ATTRACT(30);
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
}
