package ru.tishin.starGame.game;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import ru.tishin.starGame.game.helpers.Poolable;
import ru.tishin.starGame.screen.ScreenManager;

/*
 Что умеет бот

    1. Рандомно летает по полю. При приближении к границе поля, он разворачивается.
    2. У него есть область зрения. Если в неё попадает герой, бот начинает стрелять.
    3.

 */

public class Bot extends Ship implements Poolable {
    private Hero hero;
    private boolean active;
    private float timer;
    private float randomAngle;

    public Bot(GameController gameController) {
        super(gameController);
        active = false;
        position = new Vector2(0, 0);
        velocity = new Vector2(0, 0);
        hero = gameController.getHero();
        hitArea = new Circle(position, 20); // todo переделать костыль

    }

    public void activate(float x, float y, float vx, float vy) {
        position.set(x, y);
        velocity.set(vx, vy);
        active = true;
        hpMax = (30 * gameController.getLevel().getCurrentLevel());
        hp = hpMax;
        angle = MathUtils.random(0f, 360f);
        randomAngle = angle;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void moving(float dt) {
        if (true) {
            //velocity.add(MathUtils.cosDeg(angle) * engineSpeed * dt, MathUtils.sinDeg(angle) * engineSpeed * dt);
            velocity.mulAdd(direction, engineSpeed * dt);
        }
//        float bx = position.x + MathUtils.cosDeg(angle + 180) * 30;
//        float by = position.y + MathUtils.sinDeg(angle + 180) * 30;
//        for (int i = 0; i < 3; i++) {
//            gameController.getParticleController().setup(bx + MathUtils.random(-4, 4), by + MathUtils.random(-4, 4),
//                    velocity.x * -0.1f + MathUtils.random(-20, 20), velocity.y * -0.1f + MathUtils.random(-20, 20),
//                    0.3f, 1.2f, 0.2f,
//                    1.0f, 0.5f, 0, 1,
//                    1, 1, 1, 0);
//        }

    }

    public void turnOutGameBounds(float dt) {
        if (position.x < 70) {
            changeDirectionToRandomAngle(-70f, 70f, dt);
        }
        if (position.y < 70) {
            changeDirectionToRandomAngle(20f, 160f, dt);
        }
        if (position.x > ScreenManager.SCREEN_WIDTH - 70) {
            changeDirectionToRandomAngle(110f, 250f, dt);
        }
        if (position.y > ScreenManager.SCREEN_HEIGHT - 70) {
            changeDirectionToRandomAngle(200f, 340f, dt);
        }
    }

    /*
    плавный переход
     */

    public void changeDirectionToRandomAngle(float x, float y, float dt) {

        if (timer > 2) {
            randomAngle = MathUtils.random(x, y);
            timer = 0;
        }

        boolean b = MathUtils.isEqual(angle, randomAngle, 10f);
        if (!b & angle < randomAngle) {
            angle += dt * 100;
        } else if (!b & angle > randomAngle) {
            angle -= dt * 100;
        }

    }


    /*
    идея состоит в следующем.
    указатель до героя у нас используется только для вычисления угла,
    на которой нужно повернуть бота и после этого продолжить движение.

    алгоритм
    1. Вычисляется вектор до героя.
    2. Вычисляется угол направления носа бота, от вектора направления бота.
    3. Используется метод, который поворачивает до тех пор, пока угол вектор направления не совпадёт с вектором до героя.
     */


    @Override
    public void update(float dt) {
        super.update(dt);
        timer += dt;
        moving(dt);
        turnOutGameBounds(dt);

        if (position.dst(hero.getPosition()) < 300) {
            rotateTo(hero.getPosition(), dt);
            shooting();
        }

    }



    public void deactivate() {
        active = false;
    }

    @Override
    public boolean takeDamage(int amount) {
        if (!checkLive()) deactivate();
        return super.takeDamage(amount);
    }
}
