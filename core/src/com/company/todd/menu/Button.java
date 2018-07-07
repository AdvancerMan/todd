package com.company.todd.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.texture.TextureRegionInfo;

public abstract class Button implements Disposable {
    protected ToddEthottGame game;
    protected ButtonFunction func;
    protected Sprite spriteClicked, spriteNotClicked;
    protected boolean clicked;

    protected Button(ButtonFunction func, ToddEthottGame game) {
        this.func = func;
        this.game = game;
        clicked = false;

        spriteNotClicked = null;
        spriteClicked = null;
    }

    public Button(ButtonFunction func, ToddEthottGame game,
                  int x, int y, int width, int height) {
        this(func, game);

        spriteClicked = new Sprite(game.regionInfos.getRegionInfo("buttonClicked").getTextureRegion());
        spriteNotClicked = new Sprite(game.regionInfos.getRegionInfo("buttonNotClicked").getTextureRegion());

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
        game.regionInfos.getRegionInfo("buttonClicked").dispose();
        game.regionInfos.getRegionInfo("buttonNotClicked").dispose();
    }
}
