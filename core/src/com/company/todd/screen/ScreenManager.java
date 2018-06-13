package com.company.todd.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Disposable;
import com.company.todd.ToddEthottGame;

import java.util.Stack;

public class ScreenManager implements Disposable {
    private Stack<Screen> screens;

    public ScreenManager(Screen firstScreen) {
        screens = new Stack<Screen>();
        screens.push(firstScreen);
    }

    public void addScreen(Screen screen) {
        if (!screens.empty()) {
            screens.peek().pause();
        }

        screens.push(screen);
    }

    public void removeScreen() {
        screens.pop().dispose();

        if (!screens.empty()) {
            screens.peek().resume();
        }
    }

    public void render(float delta) {
        screens.peek().render(delta);
    }

    @Override
    public void dispose() {
        while (!screens.empty()) {
            removeScreen();
        }
    }
}
