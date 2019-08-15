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

        player.addAnimationName(MyAnimation.AnimationType.RUN);
        player.setDuration(MyAnimation.AnimationType.RUN, 0.04f);
        for (int x = 0; x < 20 * 14; x += 20) {
            player.addFrame(MyAnimation.AnimationType.RUN,
                    new TextureRegionInfo(mng, "Run.png", x, 0, 20, 52));
        }

        player.addAnimationName(MyAnimation.AnimationType.STAY);  // TODO stay player anim
        player.addFrame(MyAnimation.AnimationType.STAY,
                new TextureRegionInfo(mng, "Static.png", 0, 0, 20, 52));

        player.addAnimationName(MyAnimation.AnimationType.JUMP);  // TODO jump player anim
        player.setPlayMode(MyAnimation.AnimationType.JUMP, Animation.PlayMode.NORMAL);
        for (int x = 0; x < 20 * 5; x += 20) {
            player.addFrame(MyAnimation.AnimationType.JUMP,
                    new TextureRegionInfo(mng, "Jump.png", x, 0, 20, 52));
        }

        player.addAnimationName(MyAnimation.AnimationType.SHOOT);  // TODO shoot player anim
        for (int x = 0; x < 240; x += 20) {
            player.addFrame(MyAnimation.AnimationType.SHOOT,
                    new TextureRegionInfo(mng, "GG2.png", x, 0, 20, 51));
        }

        animationInfos.put("player", player);
    }
}
