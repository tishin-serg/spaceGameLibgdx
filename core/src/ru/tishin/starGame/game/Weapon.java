package ru.tishin.starGame.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class Weapon {
    private GameController gameController;
    private Hero hero;
    private String tittle;
    private float fireTimer;
    private float fireReload;
    private int damage;
    private float bulletSpeed;
    private int bulletCountMax;
    private int bulletCountCurrent;

    // x - расстояние от центра
    // y - угол от направления корабля
    // z - угол стрельбы
    private Vector3[] slots;

    public Weapon(GameController gameController, Hero hero, String tittle,
                  float fireTimer, int damage, float bulletSpeed, int bulletCountMax,
                  float fireReload, Vector3[] slots) {
        this.gameController = gameController;
        this.hero = hero;
        this.tittle = tittle;
        this.fireTimer = fireTimer;
        this.damage = damage;
        this.bulletSpeed = bulletSpeed;
        this.bulletCountMax = bulletCountMax;
        this.slots = slots;
        this.bulletCountCurrent = bulletCountMax;
        this.fireReload = fireReload;
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
                x = hero.getPosition().x + MathUtils.cosDeg(hero.getAngle() + slots[i].y) * slots[i].x;
                y = hero.getPosition().y + MathUtils.sinDeg(hero.getAngle() + slots[i].y) * slots[i].x;
                vx = hero.getVelocity().x + MathUtils.cosDeg(hero.getAngle() + slots[i].z) * bulletSpeed;
                vy = hero.getVelocity().y + MathUtils.sinDeg(hero.getAngle() + slots[i].z) * bulletSpeed;
                gameController.getBulletController().setup(x, y, vx, vy);
            }
            bulletCountCurrent --;
        }
    }

    public void update(float dt) {
        fireTimer += dt;
    }

    public void takeBulletBonus(int value) {
        bulletCountCurrent += value;
        if (bulletCountCurrent > bulletCountMax) bulletCountCurrent = bulletCountMax;
    }
}
