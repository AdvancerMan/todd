package com.company.todd.game.level;

import com.badlogic.gdx.utils.Array;

import com.company.todd.game.objs.static_objs.StaticObject;
import com.company.todd.game.process.GameProcess;
import com.company.todd.launcher.ToddEthottGame;

import java.util.Stack;

public class Level {
    protected ToddEthottGame game;

    protected Stack<StaticObjectTemplate> objects;

    public Level(ToddEthottGame game) {
        this.game = game;

        objects = new Stack<StaticObjectTemplate>();
    }

    public void addObjectTemplate(StaticObjectTemplate template) {
        objects.push(template);
    }

    public boolean hasNext() {
        return objects.size() > 0;
    }

    public StaticObject nextObject(GameProcess gameProcess) {
        return objects.pop().getObject(gameProcess);
    }

    public void unpackTo(GameProcess gameProcess, Array<? super StaticObject> array) {
        while (hasNext()) {
            array.add(nextObject(gameProcess));
        }
    }

    // TODO read level from save
}
