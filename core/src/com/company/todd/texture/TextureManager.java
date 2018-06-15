package com.company.todd.texture;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;

import com.company.todd.ToddEthottGame;

import java.io.File;

public class TextureManager implements Disposable {
    private ArrayMap<String, Texture> textures;
    private ArrayMap<String, Integer> usagesMap;

    private Texture loadTexture(final String fileName) {
        return new Texture(ToddEthottGame.TEXTURES_FOLDER + File.separator + fileName);
    }

    public TextureManager() {
        textures = new ArrayMap<String, Texture>();
        usagesMap = new ArrayMap<String, Integer>();

        for (FileHandle file : Gdx.files.local(ToddEthottGame.TEXTURES_FOLDER).list()) {
            textures.put(file.name(), null);
            usagesMap.put(file.name(), 0);
        }
    }

    public TextureRegion getTextureRegion(final String fileName, final int x, final int y,
                                          final int width, final int height) {
        if (textures.get(fileName) == null) {
            textures.put(fileName, loadTexture(fileName));
        }

        usagesMap.put(fileName, usagesMap.get(fileName) + 1);
        return new TextureRegion(textures.get(fileName), x, y, width, height);
    }

    public void disposeTexture(final String fileName, final int usages) {
        usagesMap.put(fileName, usagesMap.get(fileName) - usages);

        if (usagesMap.get(fileName) <= 0) {
            usagesMap.put(fileName, 0);
            textures.get(fileName).dispose();
            textures.put(fileName, null);
        }
    }

    public void update(final float dt) { // TODO update TextureManager
        // Здесь освобождаем ресурсы текстур, которые долго не используются
        // Неиспользуемые ресурсы не освобождаются для ускоренной загрузки локаций
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
