package ru.tishin.starGame.game;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import ru.tishin.starGame.screen.utils.Assets;

public class Weapon {
    private GameController gameController;
    private Ship ship;
    private String tittle;
    private float fireTimer;
    private float fireReload;
    private int damage;
    private float bulletSpeed;
    private int bulletCountMax;
    private int bulletCountCurrent;
    private Sound sound;

    // x - расстояние от центра
    // y - угол от направления корабля
    // z - угол стрельбы
    private Vector3[] slots;

    public Weapon(GameController gameController, Ship ship, String tittle,
                  float fireTimer, int damage, float bulletSpeed, int bulletCountMax,
                  float fireReload, Vector3[] slots) {
        this.gameController = gameController;
        this.ship = ship;
        this.tittle = tittle;
        this.fireTimer = fireTimer;
        this.damage = damage;
        this.bulletSpeed = bulletSpeed;
        this.bulletCountMax = bulletCountMax;
        this.slots = slots;
        this.bulletCountCurrent = bulletCountMax;
        this.fireReload = fireReload;
        this.sound = Assets.getInstance().getAssetManager().get("audio/shoot.mp3");
    }

    public float getFireTimer() {
        return fireTimer;
    }

    public int getBulletCountMax() {
        return bulletCountMax;
    }

    public int getBulletCountCurrent() {
        return bulletCountCurrent;
    }

    public void fire() {
        if (bulletCountCurrent > 0 && fireTimer > fireReload) {
            for (int i = 0; i < slots.length; i++) {
                fireTimer = 0;
                float x, y, vx, vy;
                x = ship.getPosition().x + MathUtils.cosDeg(ship.getAngle() + slots[i].y) * slots[i].x;
                y = ship.getPosition().y + MathUtils.sinDeg(ship.getAngle() + slots[i].y) * slots[i].x;
                vx = ship.getVelocity().x + MathUtils.cosDeg(ship.getAngle() + slots[i].z) * bulletSpeed;
                vy = ship.getVelocity().y + MathUtils.sinDeg(ship.getAngle() + slots[i].z) * bulletSpeed;
                gameController.getBulletController().setup(x, y, vx, vy);
            }
            sound.play();
            bulletCountCurrent--;
        }
    }

    public void update(float dt) {
        fireTimer += dt;
    }

    public int takeBulletBonus(int value) {
        int diffBull = value;
        bulletCountCurrent += value;
        if (bulletCountCurrent > bulletCountMax) {
            diffBull = value - (bulletCountCurrent - bulletCountMax);
            bulletCountCurrent = bulletCountMax;
        }
        return diffBull;
    }

    public int getDamage() {
        return damage;
    }
}
