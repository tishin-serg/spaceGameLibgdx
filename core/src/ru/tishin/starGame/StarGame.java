package ru.tishin.starGame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.tishin.starGame.screen.GameScreen;
import ru.tishin.starGame.screen.ScreenManager;

public class StarGame extends Game {
	private SpriteBatch batch;

	@Override
	public void create () {
		this.batch = new SpriteBatch();
		ScreenManager.getInstance().init(this, batch);
		ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
	}

	@Override
	public void render () {
		float deltaTime = Gdx.graphics.getDeltaTime();
		getScreen().render(deltaTime); // рендерит только текущий экран
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
