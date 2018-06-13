package com.company.todd;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.company.todd.font.FontGenerator;
import com.company.todd.screen.MainMenuScreen;
import com.company.todd.screen.ScreenManager;

public class ToddEthottGame extends ApplicationAdapter {
	public static final String FONTS_FOLDER = "fonts";
	public static final String SAVES_FOLDER = "saves";
    public static final String TITLE = "Todd";
    public static final int WIDTH = 800;
    public static final int HEIGHT = 480;

    public SpriteBatch batch;
	public BitmapFont mainFont;
	public ScreenManager manager;

	@Override
	public void create () {
		batch = new SpriteBatch();
        mainFont = FontGenerator.generateFont("segoesc.ttf", 16, Color.BLACK);
        manager = new ScreenManager(new MainMenuScreen(this));
	}

	@Override
	public void render () {
	    Gdx.gl.glClearColor(0, 0, 0, 1); // Очищаем экран
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		manager.render(Gdx.graphics.getDeltaTime()); // Обновляем экран
	}
	
	@Override
	public void dispose () { // Освобождаем ресурсы
		batch.dispose();
		mainFont.dispose();
		manager.dispose();
	}
}
