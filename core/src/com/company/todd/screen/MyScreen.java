package com.company.todd.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.company.todd.launcher.ToddEthottGame;

public abstract class MyScreen implements Screen {
    protected ToddEthottGame game;
    protected Vector3 touchPos;

    private OrthographicCamera camera;
    private float cameraX, cameraY;
    private float cameraWidth, cameraHeight;

    public MyScreen(ToddEthottGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, ToddEthottGame.WIDTH, ToddEthottGame.HEIGHT);
        cameraX = 0;
        cameraY = 0;
        cameraWidth = ToddEthottGame.WIDTH;
        cameraHeight = ToddEthottGame.HEIGHT;

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

        cameraX += x;
        cameraY += y;
    }

    public void centerCameraAt(float x, float y) {
        x -= cameraWidth / 2;
        y -= cameraHeight / 2;
        translateCamera(x - cameraX, y - cameraY);
    }

    public void setCameraViewportSize(float width, float height) {
        camera.setToOrtho(false, width, height);
        camera.update();
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
