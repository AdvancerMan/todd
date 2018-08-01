package com.company.todd.game.static_objs;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import com.badlogic.gdx.utils.Disposable;
import com.company.todd.game.process.GameProcess;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.texture.TextureRegionInfo;
import com.company.todd.util.FloatCmp;

import static com.company.todd.util.FloatCmp.less;
import static com.company.todd.util.FloatCmp.more;

public class Platform extends StaticObject {  // TODO Platform
    PlatformType type;

    // if region size does not fit platform size
    TextureRegion upRight, upDown, upRightAndDown;
    TextureRegion downRight, downDown, downRightAndDown;

    public Platform(ToddEthottGame game, GameProcess gameProcess, PlatformType type,
                    float x, float y, int width, int height) {
        super(game, gameProcess);
        this.type = type;
        type.init();

        setPosition(x, y);
        setSize(width, height);
    }

    private void updateRegions() {
        Rectangle thisRect = getRect();
        int width = Math.round(thisRect.width);
        int height = Math.round(thisRect.height);

        upRight = new TextureRegion(
                type.upperTextureRegion, 0, 0,
                width % (int)type.width, (int)type.height
        );

        upDown = new TextureRegion(
                type.upperTextureRegion, 0, 0,
                (int)type.width, height % (int)type.height
        );

        upRightAndDown = new TextureRegion(
                type.upperTextureRegion, 0, 0,
                width % (int)type.width, height % (int)type.height
        );

        downRight = new TextureRegion(
                type.downTextureRegion, 0, 0,
                width % (int)type.width, (int)type.height
        );

        downDown = new TextureRegion(
                type.downTextureRegion, 0, 0,
                (int)type.width, height % (int)type.height
        );

        downRightAndDown = new TextureRegion(
                type.downTextureRegion, 0, 0,
                width % (int)type.width, height % (int)type.height
        );
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void draw(SpriteBatch batch, Rectangle cameraRectangle) {
        if (getRect().overlaps(cameraRectangle)) {
            for (float y = sprite.getY() + sprite.getHeight(); more(y, sprite.getY()); y -= type.height) {

                TextureRegion right, down, rightAndDown;
                TextureRegion region;
                if (FloatCmp.equals(y, sprite.getY() + sprite.getHeight())) {
                    right = upRight;
                    down = upDown;
                    rightAndDown = upRightAndDown;
                    region = type.upperTextureRegion;
                } else {
                    right = downRight;
                    down = downDown;
                    rightAndDown = downRightAndDown;
                    region = type.downTextureRegion;
                }

                for (float x = sprite.getX(); less(x, sprite.getX() + sprite.getWidth()); x += type.width) {
                    if (cameraRectangle.overlaps(new Rectangle(x, y, type.width, type.height))) {
                        if (more(x + type.width, sprite.getX() + sprite.getWidth()) &&
                                less(y - type.height, sprite.getY())) {
                            batch.draw(rightAndDown, x, sprite.getY());
                        } else if (more(x + type.width, sprite.getX() + sprite.getWidth())) {
                            batch.draw(right, x, y - type.height);
                        } else if (less(y - type.height, sprite.getY())) {
                            batch.draw(down, x, sprite.getY());
                        } else {
                            batch.draw(region, x, y - type.height);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        updateRegions();
    }

    @Override
    public void dispose() {
        type.dispose();
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
