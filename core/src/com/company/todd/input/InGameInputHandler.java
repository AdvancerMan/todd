package com.company.todd.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ArrayMap;

import com.company.todd.launcher.ToddEthottGame;

public class InGameInputHandler implements InputProcessor {
    private static class InputRectangle extends Rectangle {
        private int activators;

        InputRectangle(int x, int y, int width, int height) {
            super(x, y, width, height);
            activators = 0;
        }

        void disactivate() {
            activators--;
        }

        void activate() {
            activators++;
        }

        boolean isActive() {
            return activators > 0;
        }
    }

    protected ArrayMap<String, InputRectangle> inputRectangles;
    protected ArrayMap<Integer, int[]> pointers;

    public InGameInputHandler() {
        Gdx.input.setInputProcessor(this);

        inputRectangles = new ArrayMap<String, InputRectangle>();
        pointers = new ArrayMap<Integer, int[]>();

        updateRectangles(ToddEthottGame.STANDART_WIDTH, ToddEthottGame.STANDART_HEIGHT);
    }

    protected void updateRectangles(int width, int height) {  // TODO input rectangles
        // TODO koef for input rects

        int[][] rects = {  // x, y, width, height
                {width / 2, 0, width / 2, height - 100},  // right
                {0, 0, width / 2, height - 100},  // left
                {0, height - 100, width, 100},  // jump
                {100, 100, 100, 100},  // shoot
                {200, 0, 100, 100}  // pause
        };
        String[] rectNames = {
                "right",
                "left",
                "jump",
                "shoot",
                "pause"
        };

        for (int i = 0; i < rects.length; i++) {
            inputRectangles.put(rectNames[i], new InputRectangle(rects[i][0], rects[i][1], rects[i][2], rects[i][3]));
        }
    }

    public boolean isGoingRight() {
        return inputRectangles.get("right").isActive();
    }

    public boolean isGoingLeft() {
        return inputRectangles.get("left").isActive();
    }

    public boolean isJumping() {
        return inputRectangles.get("jump").isActive();
    }

    public boolean isShooting() {
        return inputRectangles.get("shoot").isActive();
    }

    public boolean isPaused() {
        return inputRectangles.get("pause").isActive();
    }

    private void activate(int x, int y) {
        y = Gdx.graphics.getHeight() - y;
        Vector2 point = new Vector2(x, y);

        for (int i = 0; i < inputRectangles.size; i++) {
            InputRectangle inputRectangle = inputRectangles.getValueAt(i);
            if (inputRectangle.contains(point)) {
                inputRectangle.activate();
            }
        }

    }

    private void disactivate(int x, int y) {
        y = Gdx.graphics.getHeight() - y;
        Vector2 point = new Vector2(x, y);

        for (int i = 0; i < inputRectangles.size; i++) {
            InputRectangle inputRectangle = inputRectangles.getValueAt(i);
            if (inputRectangle.contains(point)) {
                inputRectangle.disactivate();
            }
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (pointers.containsKey(pointer)) {
            int[] point = pointers.get(pointer);
            disactivate(point[0], point[1]);
        }

        pointers.removeKey(pointer);

        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (pointers.containsKey(pointer)) {
            int[] point = pointers.get(pointer);
            disactivate(point[0], point[1]);
        }
        activate(screenX, screenY);

        pointers.put(pointer, new int[] {screenX, screenY});

        return true;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public void resize(int width, int height) {
        updateRectangles(width, height);
    }
}
