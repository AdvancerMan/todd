package com.company.todd.menu;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.company.todd.launcher.ToddEthottGame;

public class ImageButton extends Button {
    private String clickedFileToDispose;
    private String notClickedFileToDispose;

    // You must call setImage methods after creating object
    public ImageButton(ButtonFunction<ToddEthottGame> func, ToddEthottGame game,
                       int x, int y, int width, int height) {
        super(func, game);

        spriteNotClicked = new Sprite();
        spriteNotClicked.setBounds(x, y, width, height);

        spriteClicked = new Sprite();
        spriteClicked.setBounds(x, y, width, height);

        clickedFileToDispose = null;
        notClickedFileToDispose = null;
    }

    private void setImage(String imageFileName,
                         int imageX, int imageY,
                         int imageWidth, int imageHeight,
                          Sprite sprite, String fileToDispose) {
        if (fileToDispose != null) {
            game.textureManager.disposeTexture(fileToDispose, 1);
        }
        fileToDispose = imageFileName;

        TextureRegion imageTextureRegion = game.textureManager.
                getTextureRegion(imageFileName, imageX, imageY, imageWidth, imageHeight);

        sprite.setRegion(imageTextureRegion);
    }

    public void setClickedImage(String imageFileName,
                                int imageX, int imageY,
                                int imageWidth, int imageHeight) {
        setImage(imageFileName, imageX, imageY, imageWidth, imageHeight,
                spriteClicked, clickedFileToDispose);
    }

    public void setNotClickedImage(String imageFileName,
                                int imageX, int imageY,
                                int imageWidth, int imageHeight) {
        setImage(imageFileName, imageX, imageY, imageWidth, imageHeight,
                spriteNotClicked, notClickedFileToDispose);
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (spriteNotClicked == null || spriteClicked == null) {
            throw new ImageNotSetException();
        }

        super.draw(batch);
    }

    @Override
    public void dispose() {
        if (clickedFileToDispose != null) {
            game.textureManager.disposeTexture(clickedFileToDispose, 1);
        }

        if (notClickedFileToDispose != null) {
            game.textureManager.disposeTexture(notClickedFileToDispose, 1);
        }
    }
}

class ImageNotSetException extends RuntimeException {
}
