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

    public TextureRegionGetter getRegionGetter() {
        return new TextureRegionGetter(this);
    }

    public int getWidth() {
        return regionWidth;
    }

    public int getHeight() {
        return regionHeight;
    }

    public String getTextureName() {
        return fileName;
    }

    @Override
    public void dispose() {
        usages--;

        if (usages == 0) {
            manager.disposeTexture(fileName, 1);
            region = null;
        }
    }

    public class TextureRegionGetter implements Disposable {
        private boolean gotRegion;
        private boolean usedRegion;
        private TextureRegionInfo regionInfo;
        private TextureRegion region;

        public TextureRegionGetter(TextureRegionInfo regionInfo) {
            this.regionInfo = regionInfo;
            gotRegion = false;
            usedRegion = false;
        }

        public TextureRegion getRegion() {
            if (usedRegion) {
                throw new RegionGetterException("trying to get used region");
            }

            if (!gotRegion) {
                gotRegion = true;
                region = regionInfo.getTextureRegion();
            }

            return region;
        }

        @Override
        public void dispose() {
            if (!gotRegion) {
                return;
                // throw new RegionGetterException("trying to dispose region that is not got");
            }

            if (usedRegion) {
                throw new RegionGetterException("trying to dispose used region");
            }

            gotRegion = false;
            usedRegion = true;
            regionInfo.dispose();
        }

        public class RegionGetterException extends RuntimeException {
            public RegionGetterException(String msg) {
                super(msg);
            }
        }
    }
}
