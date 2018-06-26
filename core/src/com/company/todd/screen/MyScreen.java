package com.company.todd.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.company.todd.launcher.ToddEthottGame;

public abstract class MyScreen implements Screen {
    protected ToddEthottGame game;
    protected OrthographicCamera camera;
    protected Vector3 touchPos;

    public MyScreen(ToddEthottGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, ToddEthottGame.WIDTH, ToddEthottGame.HEIGHT);

        touchPos = new Vector3();
    }

    protected void update(float delta) {
        camera.update();

        touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(touchPos);
    }

    protected void draw(SpriteBatch batch) {
        batch.setProjectionMatrix(camera.combined);
    }

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
