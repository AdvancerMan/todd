package com.company.todd.menu;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.company.todd.launcher.ToddEthottGame;

public class ImageButton extends Button {
    private Sprite image;
    private String fileToDispose;

    // You must call setImage after creating object
    public ImageButton(ButtonFunction<ToddEthottGame> func, ToddEthottGame game,
                       int x, int y, int buttonWidth, int buttonHeight) {
        super(func, game, x, y, buttonWidth, buttonHeight);

        image = null;
    }

    public void setImage(String imageFileName,
                         int imageX, int imageY,
                         int imageWidth, int imageHeight,
                         int spriteWidth, int spriteHeight) {
        if (image != null) {
            game.textureManager.disposeTexture(fileToDispose, 1);
        }

        TextureRegion imageTextureRegion = game.textureManager.
                getTextureRegion(imageFileName, imageX, imageY, imageWidth, imageHeight);
        fileToDispose = imageFileName;

        image = new Sprite(imageTextureRegion);
        image.setSize(spriteWidth, spriteHeight);

        float x = spriteNotClicked.getX() + spriteNotClicked.getWidth() / 2 - spriteWidth / 2;
        float y = spriteNotClicked.getY() + spriteNotClicked.getHeight() / 2 - spriteHeight / 2;
        image.setPosition(x, y);
    }

    public void setImage(String imageFileName,
                         int imageX, int imageY,
                         int imageWidth, int imageHeight) {
        setImage(imageFileName, imageX, imageY, imageWidth, imageHeight,
                (int)spriteNotClicked.getWidth() - 4,
                (int)spriteNotClicked.getHeight() - 4);
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);

        if (image == null) {
            throw new ImageNotSetException();
        }
        image.draw(batch);
    }

    @Override
    public void dispose() {
        super.dispose();

        game.textureManager.disposeTexture(fileToDispose, 1);
    }
}

class ImageNotSetException extends RuntimeException {
}