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
        AnimationInfo player = new AnimationInfo();

        player.addAnimationName("run");
        for (int x = 0; x < 20 * 14; x += 20) {
            player.addFrame("run",
                    new TextureRegionInfo(mng, "Run.png", x, 0, 20, 52));
        }

        player.addAnimationName("stay");  // TODO stay player anim
        player.addFrame("stay",
                new TextureRegionInfo(mng, "Static.png", 0, 0, 20, 52));

        player.addAnimationName("jump");  // TODO jump player anim
        player.setPlayMode("jump", Animation.PlayMode.NORMAL);
        for (int x = 0; x < 20 * 5; x += 20) {
            player.addFrame("jump",
                    new TextureRegionInfo(mng, "Jump.png", x, 0, 20, 52));
        }

        player.addAnimationName("shoot");  // TODO shoot player anim
        for (int x = 0; x < 240; x += 20) {
            player.addFrame("shoot",
                    new TextureRegionInfo(mng, "GG2.png", x, 0, 20, 51));
        }

        animationInfos.put("player", player);
    }
}
