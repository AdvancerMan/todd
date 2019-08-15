package com.company.todd.texture;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.company.todd.game.animations.MyAnimation;

public class AnimationInfo {
    public static Float DEFAULT_ANIMATION_DURATION = 0.1f;
    public static Animation.PlayMode DEFAULT_ANIMATION_PLAY_MODE = Animation.PlayMode.LOOP;

    private ArrayMap<MyAnimation.AnimationType, Array<TextureRegionInfo>> regionInfos;
    private ArrayMap<MyAnimation.AnimationType, Float> frameDurations;
    private ArrayMap<MyAnimation.AnimationType, Animation.PlayMode> playModes;

    public AnimationInfo(ArrayMap<MyAnimation.AnimationType, Array<TextureRegionInfo>> regionInfos,
                         ArrayMap<MyAnimation.AnimationType, Float> frameDurations,
                         ArrayMap<MyAnimation.AnimationType, Animation.PlayMode> playModes) {
        this.frameDurations = frameDurations;
        this.playModes = playModes;
        this.regionInfos = regionInfos;
    }

    public AnimationInfo() {
        this(new ArrayMap<MyAnimation.AnimationType, Array<TextureRegionInfo>>(),
                new ArrayMap<MyAnimation.AnimationType, Float>(),
                new ArrayMap<MyAnimation.AnimationType, Animation.PlayMode>());
    }

    public void addAnimationName(MyAnimation.AnimationType type) {
        regionInfos.put(type, new Array<TextureRegionInfo>());
        frameDurations.put(type, DEFAULT_ANIMATION_DURATION);
        playModes.put(type, DEFAULT_ANIMATION_PLAY_MODE);
    }

    public void addFrames(MyAnimation.AnimationType animationType, Array<TextureRegionInfo> frameInfos) {
        regionInfos.get(animationType).addAll(frameInfos);
    }

    public void addFrame(MyAnimation.AnimationType animationType, TextureRegionInfo frameInfo) {
        regionInfos.get(animationType).add(frameInfo);
    }

    public void setDuration(MyAnimation.AnimationType type, Float dur) {
        frameDurations.put(type, dur);
    }

    public void setPlayMode(MyAnimation.AnimationType type, Animation.PlayMode playMode) {
        playModes.put(type, playMode);
    }

    public MyAnimation getAnimation() {
        ArrayMap<MyAnimation.AnimationType, Array<TextureRegionInfo.TextureRegionGetter>> allGetters =
                new ArrayMap<MyAnimation.AnimationType, Array<TextureRegionInfo.TextureRegionGetter>>();

        for (ObjectMap.Entry entry : regionInfos.entries()) {
            Array<TextureRegionInfo.TextureRegionGetter> getters =
                    new Array<TextureRegionInfo.TextureRegionGetter>();

            for (TextureRegionInfo info : (Array<TextureRegionInfo>) entry.value) {
                getters.add(info.getRegionGetter());
            }

            allGetters.put((MyAnimation.AnimationType) entry.key, getters);
        }

        return new MyAnimation(frameDurations, playModes, allGetters);
    }
}
