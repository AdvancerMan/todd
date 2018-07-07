package com.company.todd.menu;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.texture.TextureRegionInfo;

public class ImageButton extends Button {
    private TextureRegionInfo clickedRegionInfo;
    private TextureRegionInfo notClickedRegionInfo;

    // You must call setImage methods after creating object
    public ImageButton(ButtonFunction func, ToddEthottGame game,
                       int x, int y, int width, int height) {
        super(func, game);

        spriteNotClicked = new Sprite();
        spriteNotClicked.setBounds(x, y, width, height);

        spriteClicked = new Sprite();
        spriteClicked.setBounds(x, y, width, height);

        clickedRegionInfo = null;
        notClickedRegionInfo = null;
    }

    public void setClickedImage(TextureRegionInfo regionInfo) {
        if (clickedRegionInfo != null) {
            clickedRegionInfo.dispose();
        }
        clickedRegionInfo = regionInfo;
        spriteClicked.setRegion(regionInfo.getTextureRegion());
    }

    public void setNotClickedImage(TextureRegionInfo regionInfo) {
        if (notClickedRegionInfo != null) {
            notClickedRegionInfo.dispose();
        }
        notClickedRegionInfo = regionInfo;
        spriteClicked.setRegion(regionInfo.getTextureRegion());
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (clickedRegionInfo == null || notClickedRegionInfo == null) {
            throw new ImageNotSetException();
        }

        super.draw(batch);
    }

    @Override
    public void dispose() {
        if (clickedRegionInfo != null) {
            clickedRegionInfo.dispose();
        }

        if (notClickedRegionInfo != null) {
            notClickedRegionInfo.dispose();
        }
    }
}

class ImageNotSetException extends RuntimeException {
}
