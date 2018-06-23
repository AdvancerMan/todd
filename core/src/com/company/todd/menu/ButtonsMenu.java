package com.company.todd.menu;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.company.todd.launcher.ToddEthottGame;

import java.util.Iterator;

public class ButtonsMenu {
    private Array<Button> buttons;

    public ButtonsMenu(ToddEthottGame game, Array<String> buttonTextArray,
                       Array<ButtonFunction<ToddEthottGame>> buttonFunctions,
                       int x, int y, int width, int height, int spaceBetweenButtons) {
        int buttonWidth = width;
        int buttonHeight = (height - spaceBetweenButtons * (buttonFunctions.size - 1)) / buttonFunctions.size;

        buttons = new Array<Button>();

        buttonTextArray.reverse();
        buttonFunctions.reverse();
        Iterator<ButtonFunction<ToddEthottGame>> functionIterator = buttonFunctions.iterator();
        Iterator<String> textIterator = buttonTextArray.iterator();

        while (functionIterator.hasNext() && textIterator.hasNext()) {
            buttons.add(new TextButton(functionIterator.next(), game,
                    x, y, buttonWidth, buttonHeight, textIterator.next()));
            y += buttonHeight + spaceBetweenButtons;
        }
    }

    public void update(float delta, float touchX, float touchY) {
        for (Button button : buttons) {
            button.update(delta, touchX, touchY);
        }
    }

    public void draw(SpriteBatch batch) {
        for (Button button : buttons) {
            button.draw(batch);
        }
    }

    public void dispose() {
        for (Button button : buttons) {
            button.dispose();
        }
    }
}