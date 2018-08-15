package com.company.todd.game.level;

import com.badlogic.gdx.utils.Disposable;

import com.company.todd.game.objs.static_objs.StaticObject;
import com.company.todd.game.process.GameProcess;
import com.company.todd.launcher.ToddEthottGame;

import java.util.Stack;

public class Level implements Disposable {  // TODO Level
    protected ToddEthottGame game;
    protected GameProcess gameProcess;

    protected Stack<StaticObject> objects;

    public Level(ToddEthottGame game, GameProcess gameProcess) {
        this.game = game;
        this.gameProcess = gameProcess;
    }

    public void addObject(StaticObject object) {
        objects.push(object);
    }

    public boolean hasNext() {
        return objects.size() > 0;
    }

    public StaticObject nextObject() {
        return objects.pop();
    }

    // TODO read level from save

    @Override
    public void dispose() {
        while (hasNext()) {
            nextObject().dispose();
        }
    }
}
