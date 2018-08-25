package com.company.todd.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Disposable;
import com.company.todd.launcher.ToddEthottGame;

import java.util.Stack;

public class ScreenManager implements Disposable {
    protected Stack<Screen> screens;
    protected boolean screenRemoved;
    protected int screenWidth, screenHeight;

    public ScreenManager(Screen firstScreen) {
        screens = new Stack<Screen>();
        screens.push(firstScreen);

        screenHeight = ToddEthottGame.STANDART_HEIGHT;
        screenWidth = ToddEthottGame.STANDART_WIDTH;
        firstScreen.resize(screenWidth, screenHeight);
    }

    public void setNextScreen(Screen screen) {
        if (!screens.empty()) {
            screens.peek().pause();
        }

        screen.resize(screenWidth, screenHeight);
        screens.push(screen);
    }

    public void removeThisScreen() {
        screenRemoved = true;
    }

    private void removeScreen() {
        screens.pop().dispose();

        if (!screens.empty()) {
            screens.peek().resume();
            screens.peek().resize(screenWidth, screenHeight);
        }
    }

    private void update() {
        if (screenRemoved) {
            screenRemoved = false;
            removeScreen();
        }
    }

    public void render(float delta) {
        screens.peek().render(delta);
        update();
    }

    public void resize(int width, int height) {
        screenWidth = width;
        screenHeight = height;
        screens.peek().resize(screenWidth, screenHeight);
    }

    public void pause() {
        screens.peek().pause();
    }

    public void resume() {
        screens.peek().resume();
    }

    @Override
    public void dispose() {
        while (!screens.empty()) {
            screens.pop().dispose();
        }
    }
}
