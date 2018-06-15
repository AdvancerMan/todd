package com.company.todd.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.company.todd.ToddEthottGame;
import com.company.todd.game.Save;
import com.company.todd.input.InGameInputHandler;

public class GameScreen implements Screen { // TODO GameScreen
    private ToddEthottGame game;
    private InGameInputHandler inputHandler;
    private OrthographicCamera camera;
    private TextureRegion texture;
    private int x = 321, y = 123;

    public GameScreen(ToddEthottGame game_) {
        game = game_;

        inputHandler = new InGameInputHandler();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        texture = game.textureManager.getTextureRegion("badlogic.jpg", 10, 10, 64, 64);
    }

    public GameScreen(ToddEthottGame game_, Save save) {
        this(game_);
    }

    @Override
    public void show() {
        
    }

    private void handleInput(float delta) {
        if (Gdx.input.isTouched()) {
            inputHandler.setNewTouchPosition(Gdx.input.getX(), Gdx.input.getY());

            if (inputHandler.isGoingLeft()) {
                x -= delta * 200;
            }
            if (inputHandler.isGoingRight()) {
                x += delta * 200;
            }
        }

        if (Gdx.input.justTouched()) {
            if (inputHandler.isPaused()) {
                game.screenManager.removeScreen();
            }
        }
    }

    @Override
    public void render(float delta) {
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(texture, x, y);
        game.batch.end();

        handleInput(delta);
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

    }
}
