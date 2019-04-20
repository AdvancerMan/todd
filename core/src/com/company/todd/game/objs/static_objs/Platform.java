package com.company.todd.game.objs.static_objs;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Disposable;
import com.company.todd.debug.DebugTimer;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.texture.TextureRegionInfo;

import static com.company.todd.util.FloatCmp.less;

public class Platform extends StaticObject {
    protected Type type;
    protected Texture tmpTexture;  // TODO move tmpTexture to TextureManager

    public Platform(ToddEthottGame game, Type type,
                    float x, float y, float width, float height) {
        super(game, x, y, width, height);
        this.type = type;
        type.init();

        tmpTexture = null;

        updateTexture();
    }

    private void updateTexture() {  // TODO many platforms, one texture, many TextureRegions (move tmpTexture to TextureManager)
        if (tmpTexture != null) {
            tmpTexture.dispose();  // TODO move tmpTexture to TextureManager
        }

        Pixmap upPixmap, downPixmap;

        TextureData tmpData = type.upperTextureRegion.getTexture().getTextureData();
        tmpData.prepare();
        upPixmap = tmpData.consumePixmap();

        TextureData tmpData2 = null;
        if (type.upperRegionInfo.getTextureName().equals(type.downRegionInfo.getTextureName())) {
            downPixmap = upPixmap;
        } else {
            tmpData2 = type.downTextureRegion.getTexture().getTextureData();
            tmpData2.prepare();
            downPixmap = tmpData2.consumePixmap();
        }

        int width = (int)sprite.getWidth(), height = (int)sprite.getHeight();
        Pixmap finalPixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);

        DebugTimer timer = new DebugTimer("platform - updateTexture() - drawing on Pixmap");
        timer.start();

        finalPixmap.drawPixmap(upPixmap, 0, 0);
        finalPixmap.drawPixmap(downPixmap, 0, type.height);

        for (int rBound = type.width; less(rBound, width); rBound *= 2) {
            finalPixmap.drawPixmap(finalPixmap, rBound, 0, 0, 0, rBound, type.height);
        }

        for (int rBound = type.width, dBound = type.height;
             less(rBound, width) || less(dBound, height);
             rBound *= 2, dBound *= 2) {
            if (less(rBound, width)) {  // TODO optimize (min(dBound, height - type.height) etc.)
                finalPixmap.drawPixmap(finalPixmap, rBound, type.height, 0, type.height, rBound, Math.min(dBound, height - type.height));
            }
            if (less(dBound, height)) {
                finalPixmap.drawPixmap(finalPixmap, 0, dBound + type.height, 0, type.height, rBound, dBound);
            }
            if (less(rBound, width) && less(dBound, height)) {
                finalPixmap.drawPixmap(finalPixmap, rBound, dBound + type.height, 0, type.height, rBound, dBound);
            }
        }

        timer.finish();

        tmpTexture = new Texture(finalPixmap);  // TODO move tmpTexture to TextureManager

        finalPixmap.dispose();
        if (tmpData.disposePixmap()) {
            upPixmap.dispose();
        }
        if (tmpData2 != null && tmpData2.disposePixmap()) {
            downPixmap.dispose();
        }

        sprite.setTexture(tmpTexture);
        sprite.setRegionX(0);
        sprite.setRegionY(0);
        sprite.setRegionWidth(tmpTexture.getWidth());
        sprite.setRegionHeight(tmpTexture.getHeight());
    }

    @Override
    public void update(float delta) {}

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        updateTexture();
    }

    @Override
    public void dispose() {
        super.dispose();
        type.dispose();
        if (tmpTexture != null) {  // TODO move tmpTexture to TextureManager
            tmpTexture.dispose();
        }
    }

    public static class Type implements Disposable {
        int width, height;

        // both regions must have same size
        TextureRegionInfo upperRegionInfo, downRegionInfo;
        TextureRegion upperTextureRegion, downTextureRegion;

        public Type(TextureRegionInfo upperRegionInfo, TextureRegionInfo downRegionInfo) {
            this.upperRegionInfo = upperRegionInfo;
            this.downRegionInfo = downRegionInfo;

            upperTextureRegion = null;
            downTextureRegion = null;

            width = upperRegionInfo.getWidth();
            height = upperRegionInfo.getHeight();
        }

        public void init() {
            if (upperTextureRegion == null) {
                upperTextureRegion = upperRegionInfo.getTextureRegion();
            }

            if (downTextureRegion == null) {
                downTextureRegion = downRegionInfo.getTextureRegion();
            }
        }

        @Override
        public void dispose() {
            upperRegionInfo.dispose();
            downRegionInfo.dispose();
        }
    }

    public static class Types {
        ArrayMap<String, Type> types;

        public Types(ToddEthottGame game) {
            types = new ArrayMap<String, Type>();

            types.put(
                    "grassPlatform",
                    new Type(game.regionInfos.getRegionInfo("grassPlatformUp"),
                                     game.regionInfos.getRegionInfo("grassPlatformDown"))
            );
        }

        public Type getPlatformType(String platformTypeName) {
            return types.get(platformTypeName);
        }
    }
}
