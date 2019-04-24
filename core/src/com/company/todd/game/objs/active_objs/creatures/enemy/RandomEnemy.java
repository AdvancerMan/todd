package com.company.todd.game.objs.active_objs.creatures.enemy;

import com.badlogic.gdx.utils.TimeUtils;
import com.company.todd.game.objs.active_objs.creatures.Creature;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.texture.TextureRegionInfo;

import java.util.Random;

public class RandomEnemy extends Creature {
    private long lastActionMoment;
    private final long actionDelay;
    private final Random rnd;

    public RandomEnemy(ToddEthottGame game, TextureRegionInfo regionInfo, float jumpPower, float walkingSpeed, float runningSpeed, float x, float y, float width, float height) {
        super(game, regionInfo, jumpPower, walkingSpeed, runningSpeed, x, y, width, height);

        rnd = new Random();
        lastActionMoment = TimeUtils.nanoTime();
        actionDelay = (long)1e8 * (rnd.nextInt(10) + 1);

        jumpPower += 50 * (rnd.nextInt(7));
        maxHealthLevel += 20 * (rnd.nextInt(10) + 1);
        walkingSpeed += rnd.nextInt(10);
        coolDown *= rnd.nextInt(5) + 1;
    }

    private int actionID = -1;

    @Override
    public void update(float delta) {
        super.update(delta);

        long timeNow = TimeUtils.nanoTime();
        if (timeNow - actionDelay >= lastActionMoment) {
            lastActionMoment = timeNow;
            actionID = rnd.nextInt(4);
        }

        switch (actionID) {
            case -1:
                break;

            case 0:
                shoot();
                break;
            case 1:
                walk(true);
                changeDirection(true);
                break;
            case 2:
                walk(false);
                changeDirection(false);
                break;
            case 3:
                jump();
                break;
        }
    }
}
