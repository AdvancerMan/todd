package com.company.todd.game.animations;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Disposable;
import com.company.todd.texture.TextureRegionInfo;

public class MyAnimation implements Disposable {
    private ArrayMap<AnimationType, Animation<TextureRegion>> animations;
    private ArrayMap<AnimationType, Array<TextureRegionInfo.TextureRegionGetter>> getters;

    private AnimationType playingAnimationType;
    private float timeFromStart;
    private boolean startedNow;

    public MyAnimation(ArrayMap<AnimationType, Float> frameDurations,
                       ArrayMap<AnimationType, Animation.PlayMode> playModes,
                       ArrayMap<AnimationType, Array<TextureRegionInfo.TextureRegionGetter>> getters) {
        animations = new ArrayMap<AnimationType, Animation<TextureRegion>>();
        this.getters = new ArrayMap<AnimationType, Array<TextureRegionInfo.TextureRegionGetter>>();

        playingAnimationType = AnimationType.STAY;
        timeFromStart = 0;
        startedNow = true;

        for (AnimationType animType : getters.keys()) {
            addAnimation(animType, getters.get(animType), frameDurations.get(animType), playModes.get(animType));
        }
    }

    public MyAnimation() {
        this(new ArrayMap<AnimationType, Float>(),
                new ArrayMap<AnimationType, Animation.PlayMode>(),
                new ArrayMap<AnimationType, Array<TextureRegionInfo.TextureRegionGetter>>());
    }

    public void update(float delta) {
        if (!startedNow) {
            timeFromStart += delta;
        }
        startedNow = false;
    }

    public TextureRegion getFrame() {
        if (!animations.containsKey(playingAnimationType)) {
            throw new AnimationException("no frames of " + playingAnimationType + " type");
        }
        return animations.get(playingAnimationType).getKeyFrame(timeFromStart);
    }

    public void addAnimation(AnimationType animType, Array<TextureRegionInfo.TextureRegionGetter> getters,
                             float frameDuration, Animation.PlayMode playMode) {
        if (animations.containsKey(animType)) {
            throw new AnimationException("trying to add animation with already used type in MyAnimation.addAnimation()");
        }

        this.getters.put(animType, getters);

        Array<TextureRegion> regions = new Array<TextureRegion>();
        for (TextureRegionInfo.TextureRegionGetter getter : getters) {
            regions.add(getter.getRegion());
        }

        animations.put(animType, new Animation<TextureRegion>(frameDuration, regions, playMode));
    }

    public void deleteAnimation(AnimationType animType) {
        if (!getters.containsKey(animType)) {
            throw new AnimationException("no frames of " + animType + " type");
        }

        for (TextureRegionInfo.TextureRegionGetter getter : getters.get(animType)) {
            getter.dispose();
        }

        getters.removeKey(animType);
        animations.removeKey(animType);
    }

    public void setAnimation(AnimationType animType, Array<TextureRegionInfo.TextureRegionGetter> getters,
                             float frameDuration, Animation.PlayMode playMode) {
        if (!animations.containsKey(animType)) {
            addAnimation(animType, getters, frameDuration, playMode);
            return;
        }

        Array<TextureRegion> regions = new Array<TextureRegion>();
        for (TextureRegionInfo.TextureRegionGetter getter : getters) {
            regions.add(getter.getRegion());
        }

        deleteAnimation(animType);

        this.getters.put(animType, getters);
        animations.put(animType, new Animation<TextureRegion>(frameDuration, regions, playMode));
    }

    public void setFrameDuration(AnimationType animType, float frameDuration) {
        if (!animations.containsKey(animType)) {
            throw new AnimationException("no frames of " + animType + " type");
        }

        animations.get(animType).setFrameDuration(frameDuration);
    }

    public void setAnimationDuration(AnimationType animType, float animationDuration) {
        if (!animations.containsKey(animType)) {
            throw new AnimationException("no frames of " + animType + " type");
        }

        Animation<TextureRegion> animation = animations.get(animType);
        animation.setFrameDuration(animationDuration / animation.getKeyFrames().length);
    }

    public void setPlayMode(AnimationType animType, Animation.PlayMode playMode) {
        if (!animations.containsKey(animType)) {
            throw new AnimationException("no frames of " + animType + " type");
        }

        animations.get(animType).setPlayMode(playMode);
    }

    public void setPlayingAnimationType(AnimationType playingAnimationType, boolean changeEquals) {  // TODO priorities of animations
        if (!playingAnimationType.equals(this.playingAnimationType) || changeEquals) {
            timeFromStart = 0;
            startedNow = true;
            this.playingAnimationType = playingAnimationType;

            if (!animations.containsKey(playingAnimationType)) {
                this.playingAnimationType = AnimationType.STAY;
                System.err.println("no frames of " + playingAnimationType + " type");
            }
        }
    }

    @Override
    public void dispose() {
        for (Array<TextureRegionInfo.TextureRegionGetter> getters : this.getters.values()) {
            for (TextureRegionInfo.TextureRegionGetter getter : getters) {
                getter.dispose();
            }
        }
    }

    public enum AnimationType {
        STAY, RUN, FALL, JUMP, SHOOT
    }

    public class AnimationException extends RuntimeException {
        private AnimationException(String msg) {
            super(msg);
        }
    }
}
