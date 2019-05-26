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

    private String playingAnimationName;
    private float timeFromStart;
    private boolean startedNow;

    public MyAnimation(float frameDuration,
                       ArrayMap<String, Array<TextureRegionInfo.TextureRegionGetter>> getters,
                       Animation.PlayMode playMode) {
        animations = new ArrayMap<String, Animation<TextureRegion>>();
        this.getters = new ArrayMap<String, Array<TextureRegionInfo.TextureRegionGetter>>();

        playingAnimationName = "";
        timeFromStart = 0;
        startedNow = true;

        for (String animName : getters.keys) {
            playingAnimationName = animName;
            addAnimation(animName, getters.get(animName), frameDuration, playMode);
        }
    }

    public void update(float delta) {
        if (!startedNow) {
            timeFromStart += delta;
        }
    }

    public TextureRegion getFrame() {
        startedNow = false;
        return animations.get(playingAnimationName).getKeyFrame(timeFromStart);
    }

    public void addAnimation(String animName, Array<TextureRegionInfo.TextureRegionGetter> getters,
                             float frameDuration, Animation.PlayMode playMode) {
        this.getters.put(animName, getters);

        Array<TextureRegion> regions = new Array<TextureRegion>();
        for (TextureRegionInfo.TextureRegionGetter getter : getters) {
            regions.add(getter.getRegion());
        }

        animations.put(animName, new Animation<TextureRegion>(frameDuration, regions, playMode));
    }

    public void deleteAnimation(String animName) {
        for (TextureRegionInfo.TextureRegionGetter getter : getters.get(animName)) {
            getter.dispose();
        }

        getters.removeKey(animName);
        animations.removeKey(animName);
    }

    public void setAnimation(String animName, Array<TextureRegionInfo.TextureRegionGetter> getters,
                             float frameDuration, Animation.PlayMode playMode) {
        Array<TextureRegion> regions = new Array<TextureRegion>();
        for (TextureRegionInfo.TextureRegionGetter getter : getters) {
            regions.add(getter.getRegion());
        }

        deleteAnimation(animName);

        this.getters.put(animName, getters);
        animations.put(animName, new Animation<TextureRegion>(frameDuration, regions, playMode));
    }

    public void setFrameDuration(String animName, float frameDuration) {
        animations.get(animName).setFrameDuration(frameDuration);
    }

    public void setPlayMode(String animName, Animation.PlayMode playMode) {
        animations.get(animName).setPlayMode(playMode);
    }

    public void setPlayingAnimationName(String playingAnimationName, boolean changeEquals) {
        if (!playingAnimationName.equals(this.playingAnimationName) || changeEquals) {
            timeFromStart = 0;
            startedNow = true;
            this.playingAnimationName = playingAnimationName;

            if (!animations.containsKey(playingAnimationName)) {
                this.playingAnimationName = "stay";
            }
        }
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
