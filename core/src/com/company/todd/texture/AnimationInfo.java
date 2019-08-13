package com.company.todd.texture;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.company.todd.game.animations.MyAnimation;

public class AnimationInfo {
    public static Float DEFAULT_ANIMATION_DURATION = 0.1f;
    public static Animation.PlayMode DEFAULT_ANIMATION_PLAY_MODE = Animation.PlayMode.LOOP;

    private ArrayMap<String, Array<TextureRegionInfo>> regionInfos;
    private ArrayMap<String, Float> frameDurations;
    private ArrayMap<String, Animation.PlayMode> playModes;

    public AnimationInfo(ArrayMap<String, Array<TextureRegionInfo>> regionInfos,
                         ArrayMap<String, Float> frameDurations,
                         ArrayMap<String, Animation.PlayMode> playModes) {
        this.frameDurations = frameDurations;
        this.playModes = playModes;
        this.regionInfos = regionInfos;
    }

    public AnimationInfo() {
        this(new ArrayMap<String, Array<TextureRegionInfo>>(),
                new ArrayMap<String, Float>(),
                new ArrayMap<String, Animation.PlayMode>());
    }

    public void addAnimationName(String name) {
        regionInfos.put(name, new Array<TextureRegionInfo>());
        frameDurations.put(name, DEFAULT_ANIMATION_DURATION);
        playModes.put(name, DEFAULT_ANIMATION_PLAY_MODE);
    }

    public void addFrames(String animationName, Array<TextureRegionInfo> frameInfos) {
        regionInfos.get(animationName).addAll(frameInfos);
    }

    public void addFrame(String animationName, TextureRegionInfo frameInfo) {
        regionInfos.get(animationName).add(frameInfo);
    }

    public void setDuration(String name, Float dur) {
        frameDurations.put(name, dur);
    }

    public void setPlayMode(String name, Animation.PlayMode playMode) {
        playModes.put(name, playMode);
    }

    public MyAnimation getAnimation() {
        ArrayMap<String, Array<TextureRegionInfo.TextureRegionGetter>> allGetters =
                new ArrayMap<String, Array<TextureRegionInfo.TextureRegionGetter>>();

        for (ObjectMap.Entry entry : regionInfos.entries()) {
            Array<TextureRegionInfo.TextureRegionGetter> getters =
                    new Array<TextureRegionInfo.TextureRegionGetter>();

            for (TextureRegionInfo info : (Array<TextureRegionInfo>) entry.value) {
                getters.add(info.getRegionGetter());
            }

            allGetters.put((String) entry.key, getters);
        }

        return new MyAnimation(frameDurations, playModes, allGetters);
    }
}
