package com.company.todd.game.animations;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Disposable;
import com.company.todd.texture.TextureRegionInfo;

public class MyAnimation implements Disposable {
    private ArrayMap<String, Animation<TextureRegion>> animations;
    private ArrayMap<String, Array<TextureRegionInfo.TextureRegionGetter>> getters;

    public MyAnimation(float frameDuration,
                       ArrayMap<String, Array<TextureRegionInfo.TextureRegionGetter>> getters,
                       Animation.PlayMode playMode) {
        animations = new ArrayMap<String, Animation<TextureRegion>>();
        this.getters = new ArrayMap<String, Array<TextureRegionInfo.TextureRegionGetter>>();

        for (String animName : getters.keys) {
            this.getters.put(animName, getters.get(animName));

            Array<TextureRegion> regions = new Array<TextureRegion>();
            for (TextureRegionInfo.TextureRegionGetter getter : getters.get(animName)) {
                regions.add(getter.getRegion());
            }

            animations.put(animName, new Animation<TextureRegion>(frameDuration, regions, playMode));
        }
    }

    public void setFrameDuration(String animName, float frameDuration) {
        animations.get(animName).setFrameDuration(frameDuration);
    }

    public void setPlayMode(String animName, Animation.PlayMode playMode) {
        animations.get(animName).setPlayMode(playMode);
    }

    @Override
    public void dispose() {
        for (Array<TextureRegionInfo.TextureRegionGetter> getters : this.getters.values) {
            for (TextureRegionInfo.TextureRegionGetter getter : getters) {
                getter.dispose();
            }
        }
    }
}
