package ru.tishin.starGame.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import ru.tishin.starGame.screen.ScreenManager;
import ru.tishin.starGame.screen.utils.Assets;

public class Ship {
    protected GameController gameController;
    protected Vector2 position;
    protected Vector2 velocity;
    protected Vector2 direction;
    protected Vector2 tempVector;
    protected float angle;
    protected Circle hitArea;
    protected int hp;
    protected int hpMax;
    protected Weapon currentWeapon;
    protected Weapon[] weapons;
    protected int weaponNum;
    protected TextureRegion texture;
    protected float engineSpeed;

    public Ship(GameController gameController) {
        this.gameController = gameController;
        this.hpMax = 100;
        this.engineSpeed = 130f;
        texture = Assets.getInstance().getAtlas().findRegion("ship");
        hp = hpMax;
        angle = 0.0f;
        direction = new Vector2(0, 0);
        createWeapons();
        weaponNum = 0;
        currentWeapon = weapons[weaponNum];
        tempVector = new Vector2(0, 0);
    }

    public void update(float dt) {
        /*
        Меняем позицию корабля. Прибавляем к вектору позиции объекта вектор ускорения.
        Умножаем на скаляр (чтобы скорость не зависела от частоты фпс).
         */

        position.mulAdd(velocity, dt);
        hitArea.setPosition(position);
        currentWeapon.update(dt);
        direction.set(MathUtils.cosDeg(angle), MathUtils.sinDeg(angle));
        slowingDown(dt);
        checkGameBounds();
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

    public void shooting() {
        currentWeapon.fire();
    }

    private void createWeapons() {
        weapons = new Weapon[]{
                new Weapon(gameController, this, "Laser", 0, 1, 500f, 200, 0.2f,
                        new Vector3[]{
                                new Vector3(28, 0, 0)
                        }),
                new Weapon(gameController, this, "Laser", 0, 2, 400f, 200, 0.3f,
                        new Vector3[]{
                                new Vector3(28, -90, 0),
                                new Vector3(28, 90, 0)
                        }),
                new Weapon(gameController, this, "Laser", 0, 3, 400f, 200, 0.3f,
                        new Vector3[]{
                                new Vector3(28, 0, 0),
                                new Vector3(28, 90, 20),
                                new Vector3(28, -90, -20),
                        }),
                new Weapon(gameController, this, "Laser", 0, 3, 600f, 400, 0.2f,
                        new Vector3[]{
                                new Vector3(28, 0, 0),
                                new Vector3(28, 90, 20),
                                new Vector3(28, -90, -20),
                        }),
                new Weapon(gameController, this, "Laser", 0, 4, 600f, 500, 0.2f,
                        new Vector3[]{
                                new Vector3(28, 0, 0),
                                new Vector3(28, 90, 20),
                                new Vector3(28, -90, -20),

                        })
        };
    }

    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(texture, position.x - 32, position.y - 32, 32, 32, 64, 64,
                1, 1, direction.angleDeg());
    }

    public float angleBetween(Vector2 v1, Vector2 v2) {
        float result = (float) Math.toDegrees(MathUtils.atan2(v2.y, v2.x) - MathUtils.atan2(v1.y, v1.x));
        if (result < -180) {
            result += 360;
        } else if (result > 180) {
            result -= 180;
        }
        return result;
    }

    public Vector2 getDirectionTo(Vector2 target) {
        tempVector.set(0, 0);
        return tempVector.set(target).sub(position).nor();
    }

    public void rotateTo(Vector2 target, float dt) {
        getDirectionTo(target);
        float angleBetween = angleBetween(tempVector, direction);
        if (!MathUtils.isEqual(angleBetween, 0, 1f)) {
            System.out.println(angleBetween);
            if (angleBetween > 0) {
                angle -= 10.0f * dt * 10;
            }
            else {
                angle += 10.0f * dt * 10;
            }
            direction.rotateDeg(angle);
        }
    }

    public boolean takeDamage(int amount) {
        hp -= amount;
        return hp <= 0;
    }

     public boolean checkLive() {
        return hp > 0;
    }

    public float getAngle() {
        return angle;
    }

    public Weapon getCurrentWeapon() {
        return currentWeapon;
    }

    public int getHpMax() {
        return hpMax;
    }

    public int getHp() {
        return hp;
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
