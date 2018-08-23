package com.company.todd.game.level;

import com.badlogic.gdx.utils.Array;

import com.company.todd.game.objs.static_objs.StaticObject;
import com.company.todd.game.process.GameProcess;
import com.company.todd.launcher.ToddEthottGame;

import java.util.Stack;

public class Level {
    protected ToddEthottGame game;

    protected Stack<StaticObject> objects;

    public Level(ToddEthottGame game) {
        this.game = game;

        objects = new Stack<StaticObject>();
    }

    public void addObject(StaticObject object) {
        object.setGameProcess(null);
        objects.push(object);
    }

    public boolean hasNext() {
        return objects.size() > 0;
    }

    public StaticObject nextObject(GameProcess gameProcess) {
        StaticObject object = objects.pop();
        object.setGameProcess(gameProcess);
        return object;
    }

    public void unpackTo(GameProcess gameProcess, Array<? super StaticObject> array) {
        while (hasNext()) {
            array.add(nextObject(gameProcess));
        }
    }

    // TODO read level from save
}
