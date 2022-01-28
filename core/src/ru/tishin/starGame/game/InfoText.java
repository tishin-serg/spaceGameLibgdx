package ru.tishin.starGame.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.StringBuilder;
import ru.tishin.starGame.game.helpers.Poolable;

public class InfoText implements Poolable {
    private Color color;
    private StringBuilder text;
    private boolean active;
    private Vector2 position;
    private Vector2 velocity;
    private float time;
    private float maxTime;

    public InfoText() {
        this.text = new StringBuilder();
        this.active = false;
        this.position = new Vector2(0f, 0f);
        this.velocity = new Vector2(10f, 50f);
        this.time = 0f;
        this.maxTime = 1.5f;
        this.color = Color.GREEN;
    }

    public void setup(float x, float y, String text, Color color) {
        position.set(x, y);
        active = true;
        this.text.clear();
        this.text.append(text);
        time = 0f;
        this.color = color;
    }

    public void setup(float x, float y, StringBuilder text, Color color) {
        position.set(x, y);
        active = true;
        this.text.clear();
        this.text.append(text);
        time = 0f;
        this.color = color;
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        time += dt;
        if (time >= maxTime) {
            active = false;
        }
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public StringBuilder getText() {
        return text;
    }

    public Color getColor() {
        return color;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public float getTime() {
        return time;
    }

    public float getMaxTime() {
        return maxTime;
    }
}
