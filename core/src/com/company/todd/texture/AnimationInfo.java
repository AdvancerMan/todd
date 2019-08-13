package com.company.todd.texture;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.company.todd.game.animations.MyAnimation;

public class AnimationInfo {
    private float frameDuration;
    private Animation.PlayMode playMode;
    private ArrayMap<String, Array<TextureRegionInfo>> regionInfos;

    public AnimationInfo(float frameDuration,
                         Animation.PlayMode playMode,
                         ArrayMap<String, Array<TextureRegionInfo>> regionInfos) {
        this.frameDuration = frameDuration;
        this.playMode = playMode;
        this.regionInfos = regionInfos;
    }

    public MyAnimation getAnimation() {
        ArrayMap<String, Array<TextureRegionInfo.TextureRegionGetter>> allGetters =
                new ArrayMap<String, Array<TextureRegionInfo.TextureRegionGetter>>();

        for (ObjectMap.Entry entry : regionInfos.entries()) {
            Array<TextureRegionInfo.TextureRegionGetter> getters =
                    new Array<TextureRegionInfo.TextureRegionGetter>();

            for (TextureRegionInfo.TextureRegionGetter getter : (Array<TextureRegionInfo.TextureRegionGetter>) entry.value) {
                getters.add(getter);
            }

            allGetters.put((String) entry.key, getters);
        }

        return new MyAnimation(frameDuration, playMode, allGetters);
    }
}
