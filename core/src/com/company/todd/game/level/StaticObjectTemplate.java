package com.company.todd.game.level;

import com.badlogic.gdx.utils.Array;

import com.company.todd.game.objs.static_objs.Platform;
import com.company.todd.game.objs.static_objs.StaticObject;
import com.company.todd.game.process.GameProcess;
import com.company.todd.launcher.ToddEthottGame;

class StaticObjectTemplate {
    protected Array<Object> template;
    private int indexNow;

    public StaticObjectTemplate(Array<Object> template) {
        this.template = template;
        indexNow = 0;
    }

    protected Object next() {
        Object obj = template.get(indexNow);
        indexNow = (indexNow + 1) % template.size;
        return obj;
    }

    public StaticObject getObject(GameProcess gameProcess) {
        String name = (String) next();

        if (name.equals("Platform")) {
            return new Platform((ToddEthottGame) next(), gameProcess, (Platform.Type) next(),
                    (Float) next(), (Float) next(), (Float) next(), (Float) next());
        } else if (name.equals("Spawner")) {
            // TODO Spawner in Level
        } else {
            throw new WrongClassNameException();
        }

        return null;
    }

    private static class WrongClassNameException extends RuntimeException {}
}
