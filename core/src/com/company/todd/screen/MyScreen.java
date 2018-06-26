package com.company.todd.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.company.todd.launcher.ToddEthottGame;

public abstract class MyScreen implements Screen {
    protected ToddEthottGame game;
    protected OrthographicCamera camera;

    public MyScreen(ToddEthottGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, ToddEthottGame.WIDTH, ToddEthottGame.HEIGHT);
    }

    protected abstract void update(float delta);
    protected abstract void draw(SpriteBatch batch);

    @Override
    public void render(float delta) {
        update(delta);
        draw(game.batch);
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
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public abstract void dispose();
}
