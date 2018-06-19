package com.company.todd.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.company.todd.launcher.ToddEthottGame;

public class MainMenuScreen implements Screen { // TODO MainMenuScreen
    private ToddEthottGame game;
    private TextureRegion a;

    public MainMenuScreen(ToddEthottGame game_) {
        game = game_;
        a = game.textureManager.getTextureRegion("badlogic.jpg", 10, 10, 64, 64);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        game.batch.begin();
        game.batch.draw(a, 123, 321);
        game.batch.end();

        if (Gdx.input.justTouched()) {
            game.screenManager.addScreen(new GameScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        game.textureManager.disposeTexture("badlogic.jpg", 1);
    }
}
