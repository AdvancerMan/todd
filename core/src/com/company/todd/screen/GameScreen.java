package com.company.todd.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import com.company.todd.ToddEthottGame;
import com.company.todd.game.Save;
import com.company.todd.input.InGameInputHandler;

public class GameScreen implements Screen { // TODO GameScreen
    private ToddEthottGame game;
    private InGameInputHandler inputHandler;
    private OrthographicCamera camera;
    private TextureRegion texture;
    private int x = 321, y = 123;
    private Vector3 inputVector3;
    private Vector2 inputVector2;

    public GameScreen(ToddEthottGame game_) {
        game = game_;

        inputHandler = new InGameInputHandler();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        texture = game.textureManager.getTextureRegion("badlogic.jpg", 10, 10, 64, 64);
        inputVector3 = new Vector3();
        inputVector2 = new Vector2();
    }

    public GameScreen(ToddEthottGame game_, Save save) {
        this(game_);
    }

    @Override
    public void show() {
        
    }

    @Override
    public void render(float delta) {
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(texture, x, y);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            float inputX = Gdx.input.getX(), inputY = Gdx.input.getY();
            inputVector3.set(inputX, inputY, 0);
            camera.unproject(inputVector3);
            inputVector2.set(inputVector3.x, inputVector3.y);

            if (inputHandler.isGoingLeft(inputVector2)) {
                x -= delta * 200;
            }
            if (inputHandler.isGoingRight(inputVector2)) {
                x += delta * 200;
            }
            if (inputHandler.isPaused(inputVector2)) {
                game.screenManager.removeScreen();
            }
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
        game.textureManager.dispose(); // Временно
    }
}
