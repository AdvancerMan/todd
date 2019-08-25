package com.company.todd.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.company.todd.game.objs.InGameObject;
import com.company.todd.launcher.ToddEthottGame;

public abstract class MyScreen implements Screen {
    protected ToddEthottGame game;
    private Vector2 touchPos;

    private OrthographicCamera camera;

    public MyScreen(ToddEthottGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false);

        touchPos = new Vector2();
    }

    protected void update(float delta) {
        updateTouchPos();
    }

    public void updateTouchPos() {
        touchPos.set(Gdx.input.getX(), Gdx.input.getY());
        toScreenCoord(touchPos);
    }

    public Vector2 toScreenCoord(Vector2 vector) {
        float x = vector.x, y = vector.y;
        y = Gdx.graphics.getHeight() - y - 1;
        return vector.set(x, y);
    }

    public Vector2 fromScreenToWorldCoord(Vector2 vector) {
        float widthK = camera.viewportWidth / Gdx.graphics.getWidth();
        float heightK = camera.viewportHeight / Gdx.graphics.getHeight();

        vector.set(vector.x * widthK, vector.y * heightK);

        vector.add(camera.position.x - camera.viewportWidth / 2,
                camera.position.y - camera.viewportHeight / 2);
        return vector;
    }

    public Vector2 getTouchPos() {
        return new Vector2(touchPos);
    }

    public float getCameraViewportHeight() {
        return camera.viewportHeight;
    }

    public float getCameraViewportWidth() {
        return camera.viewportWidth;
    }

    protected void draw(SpriteBatch batch) {
        batch.setProjectionMatrix(getCameraProjectionMatrix());
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

    public void centerCameraAt(Vector2 vector) {
        centerCameraAt(vector.x, vector.y);
    }

    public void centerCameraAt(InGameObject object) {
        centerCameraAt(object.getBodyPosition());
    }

    public void setCameraViewportSize(float width, float height) {
        camera.viewportHeight = height;
        camera.viewportWidth = width;
        camera.update();
    }

    public void changeCameraViewportSize(float deltaWidth, float deltaHeight) {
        setCameraViewportSize(camera.viewportWidth + deltaWidth, camera.viewportHeight + deltaHeight);
    }

    public Matrix4 getCameraProjectionMatrix() {
        return camera.combined;
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
