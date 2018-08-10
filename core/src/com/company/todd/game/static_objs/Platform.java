package com.company.todd.game.static_objs;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import com.badlogic.gdx.utils.Disposable;
import com.company.todd.debug.DebugTimer;
import com.company.todd.game.process.GameProcess;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.texture.TextureRegionInfo;

import static com.company.todd.util.FloatCmp.less;
import static com.company.todd.util.FloatCmp.more;

public class Platform extends StaticObject {
    private PlatformType type;
    private Texture tmpTexture;

    public Platform(ToddEthottGame game, GameProcess gameProcess, PlatformType type,
                    float x, float y, int width, int height) {
        super(game, gameProcess);
        this.type = type;
        type.init();

        tmpTexture = null;

        setPosition(x, y);
        setSize(width, height);
    }

    private void updateTexture() {  // TODO many platforms, one texture, many TextureRegions
        if (tmpTexture != null) {
            tmpTexture.dispose();
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

        DebugTimer timer = new DebugTimer();
        timer.start();
        for (int y = 0; y < height; y += type.height) {
            for (int x = 0; x < width; x += type.width) {
                if (y == 0) {
                    finalPixmap.drawPixmap(
                            upPixmap, x, y,
                            type.upperTextureRegion.getRegionX(), type.upperTextureRegion.getRegionY(),
                            type.upperTextureRegion.getRegionWidth(), type.upperTextureRegion.getRegionHeight()
                    );
                } else {
                    finalPixmap.drawPixmap(
                            downPixmap, x, y,
                            type.downTextureRegion.getRegionX(), type.downTextureRegion.getRegionY(),
                            type.downTextureRegion.getRegionWidth(), type.downTextureRegion.getRegionHeight()
                    );
                }
            }
        }
        timer.finish();

        tmpTexture = new Texture(finalPixmap);

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
    public void draw(SpriteBatch batch, Rectangle cameraRectangle) {
        super.draw(batch, cameraRectangle);

        /*
        long startTime;
        if (ToddEthottGame.DEBUG) {
            startTime = System.currentTimeMillis();
        }
        */

        if (getRect().overlaps(cameraRectangle)) {
            sprite.draw(batch);
        }

        /*
        if (ToddEthottGame.DEBUG) {
            System.out.println((double)(System.currentTimeMillis() - startTime) / 1000.);
        }
        */
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        updateTexture();
    }

    @Override
    public void dispose() {
        super.dispose();

        type.dispose();
        if (tmpTexture != null) {
            tmpTexture.dispose();
        }
    }

    // TODO private
    public static class PlatformType implements Disposable {
        float width, height;

        // both regions must have same size
        TextureRegionInfo upperRegionInfo, downRegionInfo;
        TextureRegion upperTextureRegion, downTextureRegion;

        public PlatformType(TextureRegionInfo upperRegionInfo, TextureRegionInfo downRegionInfo) {
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
}
