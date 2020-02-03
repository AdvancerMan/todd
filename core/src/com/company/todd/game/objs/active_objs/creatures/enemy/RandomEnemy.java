package com.company.todd.game.objs.active_objs.creatures.enemy;

import com.badlogic.gdx.utils.TimeUtils;
import com.company.todd.game.animations.MyAnimation;
import com.company.todd.game.objs.active_objs.creatures.Creature;
import com.company.todd.game.process.GameProcess;
import com.company.todd.launcher.ToddEthottGame;

import java.util.Random;

public class RandomEnemy extends Creature {
    private long lastActionMoment;
    private final long actionDelay;
    private final Random rnd;

    public RandomEnemy(ToddEthottGame game, GameProcess gameProcess, MyAnimation animation,
                       float x, float y, float width, float height) {
        super(game, gameProcess, animation, 50, 10, x, y, width, height);

        rnd = new Random();
        lastActionMoment = TimeUtils.nanoTime();
        actionDelay = (long)1e8 * (rnd.nextInt(10) + 1);

        jumpPower += 10 * (rnd.nextInt(6));
        maxHealthLevel += 20 * (rnd.nextInt(10) + 1);
        runningSpeed += rnd.nextInt(10);
        cooldown *= rnd.nextInt(5) + 1;
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

        if (actionID == 0) {
            shoot();
        } else if (actionID == 1) {
            setDirToRight(true);
            run();
        } else if (actionID == 2) {
            setDirToRight(false);
            run();
        } else if (actionID == 3) {
            jump();
        }
    }
}
