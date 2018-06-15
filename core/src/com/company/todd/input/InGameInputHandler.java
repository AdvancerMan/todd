package com.company.todd.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class InGameInputHandler {
    private Rectangle right;
    private Rectangle left;
    private Rectangle jump;
    private Rectangle shoot;
    private Rectangle pause;

    public InGameInputHandler() {  // TODO input rectangles
        right = new Rectangle(0, 0, 100, 100);
        left = new Rectangle(100, 0, 100, 100);
        jump = new Rectangle(0, 100, 100, 100);
        shoot = new Rectangle(100, 100, 100, 100);
        pause = new Rectangle(200, 0, 100, 100);
    }

    public boolean isGoingRight(Vector2 inputPoint) {
        return right.contains(inputPoint);
    }

    public boolean isGoingLeft(Vector2 inputPoint) {
        return left.contains(inputPoint);
    }

    public boolean isJumping(Vector2 inputPoint) {
        return jump.contains(inputPoint);
    }

    public boolean isShooting(Vector2 inputPoint) {
        return shoot.contains(inputPoint);
    }

    public boolean isPaused(Vector2 inputPoint) {
        return pause.contains(inputPoint);
    }
}
