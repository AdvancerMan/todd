package com.company.todd.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.company.todd.launcher.ToddEthottGame;

public class InGameInputHandler {
    private Rectangle right;
    private Rectangle left;
    private Rectangle jump;
    private Rectangle shoot;
    private Rectangle pause;
    private Vector2 inputPoint;

    public InGameInputHandler() {  // TODO input rectangles
        right = new Rectangle(400, 0, 400, ToddEthottGame.HEIGHT - 100);
        left = new Rectangle(0, 0, 400, ToddEthottGame.HEIGHT - 100);
        jump = new Rectangle(0, ToddEthottGame.HEIGHT - 100, ToddEthottGame.WIDTH, 100);
        shoot = new Rectangle(100, 100, 100, 100);
        pause = new Rectangle(200, 0, 100, 100);

        inputPoint = new Vector2();
    }

    public void setNewTouchPosition() {
        float inputX = Gdx.input.getX(), inputY = Gdx.input.getY();
        inputY = Gdx.graphics.getHeight() - 1 - inputY;
        inputPoint.set(inputX, inputY);
    }

    public boolean isGoingRight() {
        return right.contains(inputPoint);
    }

    public boolean isGoingLeft() {
        return left.contains(inputPoint);
    }

    public boolean isJumping() {
        return jump.contains(inputPoint);
    }

    public boolean isShooting() {
        return shoot.contains(inputPoint);
    }

    public boolean isPaused() {
        return pause.contains(inputPoint);
    }
}
