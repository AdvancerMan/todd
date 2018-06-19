package com.company.todd.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

import com.company.todd.launcher.ToddEthottGame;

public abstract class Button implements Disposable {
    public static final String BUTTON_FILE = "white.png"; // TODO button texture

    protected ToddEthottGame game;
    protected ButtonFunction<ToddEthottGame> func;
    protected Sprite spriteClicked, spriteNotClicked;
    protected boolean clicked;

    public Button(ButtonFunction<ToddEthottGame> func, ToddEthottGame game,
                  int x, int y, int width, int height) {
        this.func = func;
        this.game = game;
        clicked = false;
        TextureRegion clickedTextureRegion = game.textureManager.getTextureRegion(
                BUTTON_FILE, 5, 5, 200, 20  // TODO button texture
        );
        TextureRegion notClickedTextureRegion = game.textureManager.getTextureRegion(
                BUTTON_FILE, 5, 25, 200, 20  // TODO button texture
        );

        spriteClicked = new Sprite(clickedTextureRegion);
        spriteNotClicked = new Sprite(notClickedTextureRegion);

        spriteClicked.setPosition(x, y);
        spriteNotClicked.setPosition(x, y);
        spriteClicked.setSize(width, height);
        spriteNotClicked.setSize(width, height);
    }

    protected void handleInput(float x, float y) {
        if (Gdx.input.justTouched()) {
            if (spriteNotClicked.getBoundingRectangle().contains(x, y)) {
                func.click(game);
                clicked = true;
            }
        }
    }

    public void update(float delta, float touchX, float touchY) {
        handleInput(touchX, touchY);
    }

    public void draw(SpriteBatch batch) {
        if (clicked) {
            spriteClicked.draw(batch);
            clicked = false;
        }
        else {
            spriteNotClicked.draw(batch);
        }
    }

    public void dispose() {
        game.textureManager.disposeTexture(BUTTON_FILE, 2);
    }
}
