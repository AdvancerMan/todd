package com.company.todd.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.company.todd.ToddEthottGame;

public class MainMenuScreen implements Screen { // TODO MainMenuScreen
    private ToddEthottGame game;
    private Texture a;

    public MainMenuScreen(ToddEthottGame game_) {
        game = game_;
        a = new Texture("badlogic.jpg");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        game.batch.begin();
        game.batch.draw(a, 123, 321);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.manager.addScreen(new GameScreen(game));
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
        a.dispose();
    }
}
