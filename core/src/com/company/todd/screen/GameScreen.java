package com.company.todd.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.company.todd.ToddEthottGame;
import com.company.todd.game.Save;

public class GameScreen implements Screen { // TODO GameScreen
    private ToddEthottGame game;
    private Texture a;

    public GameScreen(ToddEthottGame game_) {
        game = game_;
        a = new Texture("badlogic.jpg");
    }

    public GameScreen(ToddEthottGame game_, Save save) {
        game = game_;
        a = new Texture("badlogic.jpg");
    }

    @Override
    public void show() {
        
    }

    @Override
    public void render(float delta) {

        game.batch.begin();
        game.batch.draw(a, 321, 123);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.manager.removeScreen();
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
