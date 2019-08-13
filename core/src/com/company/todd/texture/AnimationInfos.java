package com.company.todd.texture;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.company.todd.game.animations.MyAnimation;

import java.util.HashMap;
import java.util.Map;

public class AnimationInfos {
    private Map<String, AnimationInfo> animationInfos;
    private TextureManager mng;

    public AnimationInfos(TextureManager mng) {
        animationInfos = new HashMap<String, AnimationInfo>();
        this.mng = mng;

        addPlayer();
    }

    public MyAnimation getAnimation(String name) {
        return animationInfos.get(name).getAnimation();
    }

    private void addPlayer() {
        AnimationInfo player = new AnimationInfo(0.1f, Animation.PlayMode.LOOP);

        player.addAnimationName("run");
        for (int x = 0; x < 240; x += 20) {
            player.addFrame("run",
                    new TextureRegionInfo(mng, "GG2.png", x, 0, 20, 51));
        }

        player.addAnimationName("stay");  // TODO stay player anim
        for (int x = 0; x < 240; x += 20) {
            player.addFrame("stay",
                    new TextureRegionInfo(mng, "GG2.png", x, 0, 20, 51));
        }

        player.addAnimationName("jump");  // TODO jump player anim
        for (int x = 0; x < 240; x += 20) {
            player.addFrame("jump",
                    new TextureRegionInfo(mng, "GG2.png", x, 0, 20, 51));
        }

        player.addAnimationName("shoot");  // TODO shoot player anim
        for (int x = 0; x < 240; x += 20) {
            player.addFrame("shoot",
                    new TextureRegionInfo(mng, "GG2.png", x, 0, 20, 51));
        }

        animationInfos.put("player", player);
    }
}
