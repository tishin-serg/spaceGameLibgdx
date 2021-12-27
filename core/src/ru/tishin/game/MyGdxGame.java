package ru.tishin.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private Background background;
	private Hero hero;

	public Hero getHero() {
		return hero;
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Background(this);
		hero = new Hero();
	}

	@Override
	public void render () {
		float deltaTime = Gdx.graphics.getDeltaTime();
		update(deltaTime);
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		background.render(batch);
		hero.render(batch);
		batch.end();
	}

	public void update(float dt) {
		background.update(dt);
		hero.update(dt);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
