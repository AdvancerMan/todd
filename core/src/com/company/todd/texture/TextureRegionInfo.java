package com.company.todd.texture;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

public class TextureRegionInfo implements Disposable {
    private final String fileName;
    private final int regionX, regionY;
    private final int regionWidth, regionHeight;

    private final TextureManager manager;
    private TextureRegion region;
    private int usages;

    public TextureRegionInfo(TextureManager manager, String fileName,
                             int regionX, int regionY,
                             int regionWidth, int regionHeight) {
        this.fileName = fileName;
        this.regionX = regionX;
        this.regionY = regionY;
        this.regionWidth = regionWidth;
        this.regionHeight = regionHeight;

        this.manager = manager;

        region = null;
        usages = 0;
    }

    public TextureRegion getTextureRegion() {
        if (usages == 0) {
            region = manager.getTextureRegion(fileName, regionX, regionY, regionWidth, regionHeight);
        }

        usages++;
        return region;
    }

    @Override
    public void dispose() {
        usages--;
        manager.disposeTexture(fileName, 1);

        if (usages == 0) {
            region = null;
        }
    }
}
