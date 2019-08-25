package com.company.todd.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.menu.ButtonFunction;
import com.company.todd.menu.ButtonFunctions;
import com.company.todd.menu.ButtonsMenu;

public class MainMenuScreen extends MyScreen {
    private ButtonsMenu buttonsMenu;
    // TODO game title in main menu and background
    /*
    private TextureRegion background;
    private TextureRegion gameTitle;
     */


    public MainMenuScreen(ToddEthottGame game) {
        super(game);
        createButtonsMenu();
    }

    private void createButtonsMenu() {
        Array<ButtonFunction> buttonFunctions = new Array<ButtonFunction>();
        Array<String> buttonTextArray = new Array<String>();

        buttonTextArray.add("Play", "Settings", "Authors");

        buttonFunctions.add(
                new ButtonFunctions.AddSavedGameScreenFunction(),

                // TODO settings and authors screens
                new ButtonFunctions.RemoveScreenFunction(),
                new ButtonFunctions.RemoveScreenFunction()
        );

        buttonsMenu = new ButtonsMenu(game, buttonTextArray, buttonFunctions,
                150, 25, 500, 300, 10);
    }

    @Override
    protected void update(float delta) {
        super.update(delta);

        Vector2 touchPos = getTouchPos();
        buttonsMenu.update(delta, touchPos.x, touchPos.y);
    }

    @Override
    protected void draw(SpriteBatch batch) {
        super.draw(batch);

        batch.begin();

        buttonsMenu.draw(batch);

        batch.end();
    }

    @Override
    public void dispose() {
        buttonsMenu.dispose();
    }
}
