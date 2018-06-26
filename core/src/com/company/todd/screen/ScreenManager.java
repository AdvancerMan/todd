package com.company.todd.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Disposable;

import java.util.Stack;

public class ScreenManager implements Disposable {
    private Stack<Screen> screens;
    private boolean screenRemoved;

    public ScreenManager(Screen firstScreen) {
        screens = new Stack<Screen>();
        screens.push(firstScreen);
    }

    public void setNextScreen(Screen screen) {
        if (!screens.empty()) {
            screens.peek().pause();
        }

        screens.push(screen);
    }

    public void removeThisScreen() {
        screenRemoved = true;
    }

    private void removeScreen() {
        screens.pop().dispose();

        if (!screens.empty()) {
            screens.peek().resume();
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

    @Override
    public void dispose() {
        while (!screens.empty()) {
            removeThisScreen();
        }
    }
}
