package com.company.todd.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import com.company.todd.game.InGameObject;
import com.company.todd.launcher.ToddEthottGame;

public abstract class MyScreen implements Screen {
    protected ToddEthottGame game;
    protected Vector3 touchPos;

    private OrthographicCamera camera;

    public MyScreen(ToddEthottGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false);

        touchPos = new Vector3();
    }

    protected void update(float delta) {
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

    public Rectangle getCameraRect() {
        return new Rectangle(
                camera.position.x - camera.viewportWidth / 2,
                camera.position.y - camera.viewportHeight / 2,
                camera.viewportWidth, camera.viewportHeight
        );
    }

    public void translateCamera(float x, float y) {
        camera.translate(x, y);
        camera.update();
    }

    public void centerCameraAt(float x, float y) {
        translateCamera(x - camera.position.x, y - camera.position.y);
    }

    public void centerCameraAt(InGameObject object) {
        Rectangle objectRect = object.getRect();
        float x = objectRect.getX(), y = objectRect.getY();
        float width = objectRect.getWidth(), height = objectRect.getHeight();

        centerCameraAt(x + width / 2, y + height / 2);
    }

    public void setCameraViewportSize(float width, float height) {
        camera.viewportHeight = height;
        camera.viewportWidth = width;
        camera.update();
    }

    public void changeCameraViewportSize(float deltaWidth, float deltaHeight) {
        setCameraViewportSize(camera.viewportWidth + deltaWidth, camera.viewportHeight + deltaHeight);
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
