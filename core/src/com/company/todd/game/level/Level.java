package com.company.todd.game.level;

import com.badlogic.gdx.utils.Array;

import com.company.todd.game.objs.static_objs.StaticObject;
import com.company.todd.game.process.GameProcess;
import com.company.todd.launcher.ToddEthottGame;

import java.util.Stack;

public class Level {
    protected ToddEthottGame game;

    // not initialized objects
    protected Stack<StaticObject> objects;

    public Level(ToddEthottGame game) {
        this.game = game;

        objects = new Stack<StaticObject>();
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

    public void unpackTo(Array<? super StaticObject> array) {
        while (hasNext()) {
            array.add(nextObject());
        }
    }

    // TODO read level from save
}
