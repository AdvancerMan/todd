package com.company.todd.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import com.company.todd.launcher.ToddEthottGame;

public class InGameInputHandler {
    public static final int maxFingersOnScreenCount = 5;
    private Rectangle right;
    private Rectangle left;
    private Rectangle jump;
    private Rectangle shoot;
    private Rectangle pause;
    private Vector2[] inputPoints;

    public InGameInputHandler() {  // TODO input rectangles
        right = new Rectangle(400, 0, 400, ToddEthottGame.HEIGHT - 100);
        left = new Rectangle(0, 0, 400, ToddEthottGame.HEIGHT - 100);
        jump = new Rectangle(0, ToddEthottGame.HEIGHT - 100, ToddEthottGame.WIDTH, 100);
        shoot = new Rectangle(100, 100, 100, 100);
        pause = new Rectangle(200, 0, 100, 100);

        inputPoints = new Vector2[maxFingersOnScreenCount];

        for (int i = 0; i < maxFingersOnScreenCount; i++) {
            inputPoints[i] = new Vector2();
        }
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
}
