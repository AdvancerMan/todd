package com.company.todd.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import com.company.todd.launcher.ToddEthottGame;

public class InGameInputHandler {  // TODO implements InputProcessor
    public static final int maxFingersOnScreenCount = 5;
    private Rectangle right;
    private Rectangle left;
    private Rectangle jump;
    private Rectangle shoot;
    private Rectangle pause;
    private Vector2[] inputPoints;

    public InGameInputHandler() {
        updateRectangles(ToddEthottGame.STANDART_WIDTH, ToddEthottGame.STANDART_HEIGHT);

        inputPoints = new Vector2[maxFingersOnScreenCount];

        for (int i = 0; i < maxFingersOnScreenCount; i++) {
            inputPoints[i] = new Vector2();
        }
    }

    protected void updateRectangles(int width, int height) {  // TODO input rectangles
        // TODO koef for input rects

        right = new Rectangle(width / 2, 0, width / 2, height - 100);
        left = new Rectangle(0, 0, width / 2, height - 100);
        jump = new Rectangle(0, height - 100, width, 100);
        shoot = new Rectangle(100, 100, 100, 100);
        pause = new Rectangle(200, 0, 100, 100);
    }

    public void setNewTouchPosition() {
        for (int i = 0; i < maxFingersOnScreenCount; i++) {
            float inputX = Gdx.input.getX(i), inputY = Gdx.input.getY(i);
            inputY = Gdx.graphics.getHeight() - 1 - inputY;
            inputPoints[i].set(inputX, inputY);
        }
    }

    private boolean rectIsTouched(Rectangle rect, InputDetector detector) {
        for (int i = 0; i < maxFingersOnScreenCount; i++) {
            if (detector.isActive(i) && rect.contains(inputPoints[i])) {
                return true;
            }
        }
        return false;
    }

    private boolean rectIsTouched(Rectangle rect) {
        return rectIsTouched(rect, new InputDetector() {
            @Override
            public boolean isActive(int i) {
                return Gdx.input.isTouched(i);
            }
        });
    }

    public boolean isGoingRight() {
        return rectIsTouched(right);
    }

    public boolean isGoingLeft() {
        return rectIsTouched(left);
    }

    public boolean isJumping() {
        return rectIsTouched(jump);
    }

    public boolean isShooting() {
        return rectIsTouched(shoot);
    }

    public boolean isPaused() {
        return rectIsTouched(pause);
    }

    public void resize(int width, int height) {
        updateRectangles(width, height);
    }
}
