package com.company.todd.texture;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;

import com.company.todd.ToddEthottGame;

import java.io.File;

public class TextureManager implements Disposable {
    private ArrayMap<String, Texture> textures;
    //private Array<Integer> timeFromLastDispose;

    private Texture loadTexture(String fileName) {
        return new Texture(ToddEthottGame.TEXTURES_FOLDER + File.separator + fileName);
    }

    public TextureManager() {
        textures = new ArrayMap<String, Texture>();
        //timeFromLastDispose = new Array<Integer>();

        for (FileHandle file : Gdx.files.local(ToddEthottGame.TEXTURES_FOLDER).list()) {
            textures.put(file.name(), null);
        }
    }

    public TextureRegion getTextureRegion(String fileName, int x, int y, int width, int height) {
        if (textures.get(fileName) == null) {
            textures.put(fileName, loadTexture(fileName));
        }

        return new TextureRegion(textures.get(fileName), x, y, width, height);
    }

    /*
    public disposeTexture(String fileName) {

    }
    */

    public void update(float dt) {
        // Здесь освобождаем ресурсы текстур, которые долго не используются
    }

    @Override
    public void dispose() {
        for (ObjectMap.Entry<String, Texture> mapEntry : textures) {
            if (mapEntry.value != null) {
                mapEntry.value.dispose();
                textures.put(mapEntry.key, null);
            }
        }
    }
}
