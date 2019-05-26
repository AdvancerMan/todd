package com.company.todd.texture;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.company.todd.launcher.ToddEthottGame;

import java.io.File;

public class TextureManager implements Disposable {
    private ArrayMap<String, Texture> textures;
    private ArrayMap<String, Integer> usagesMap;

    private Texture loadTexture(final String fileName) {
        Texture texture;
        try {
            texture = new Texture(ToddEthottGame.TEXTURES_FOLDER + File.separator + fileName);
        } catch (GdxRuntimeException e) {
            e.printStackTrace();
            texture = new Texture(10, 10, Pixmap.Format.RGB565);
        }

        return texture;
    }

    public boolean hasTexture(final String textureName) {
        return textures.containsKey(textureName);
    }

    public void deleteTexture(final String textureName, boolean ignoreUsing) {
        if (!textures.containsKey(textureName)) {
            throw new TextureManagerException("trying to delete texture that doesn't exist");
        }

        if (textures.get(textureName) != null) {
            if (!ignoreUsing) {
                throw new TextureManagerException("trying to delete texture that is using now");
            } else {
                textures.get(textureName).dispose();
            }
        }

        usagesMap.removeKey(textureName);
        textures.removeKey(textureName);
    }

    public void addTexture(Texture texture, final String textureName, int usages) {
        textures.put(textureName, texture);
        usagesMap.put(textureName, usages);
    }

    public TextureManager() {
        textures = new ArrayMap<String, Texture>();
        usagesMap = new ArrayMap<String, Integer>();
    }

    public TextureRegion getTextureRegion(final String fileName, final int x, final int y,
                                          final int width, final int height) {
        Integer usages = usagesMap.get(fileName);
        if (usages == null || usages <= 0) {
            textures.put(fileName, loadTexture(fileName));
            usagesMap.put(fileName, 1);
        } else {
            usagesMap.put(fileName, usages + 1);
        }

        return new TextureRegion(textures.get(fileName), x, y, width, height);
    }

    public void disposeTexture(final String fileName, final int usages) {
        usagesMap.put(fileName, usagesMap.get(fileName) - usages);

        if (usagesMap.get(fileName) <= 0 && textures.get(fileName) != null) {
            usagesMap.put(fileName, 0);
            textures.get(fileName).dispose();
            textures.put(fileName, null);
        }
    }

    public void update(final float dt) { // TODO update TextureManager
        // Здесь освобождаем ресурсы текстур, которые долго не используются
        // Неиспользуемые ресурсы не освобождаются для ускоренной загрузки локаций

        /*
        if (ToddEthottGame.DEBUG) {
            for (Iterator<ObjectMap.Entry<String, Integer>> iterator = usagesMap.iterator(); iterator.hasNext(); ) {
                ObjectMap.Entry<String, Integer> next = iterator.next();
                System.out.print("{" + next.key + " " + next.value + "}" + ", ");
            }
            System.out.print("\n");
        }
        */
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

    class TextureManagerException extends RuntimeException {
        public TextureManagerException(String msg) {
            super(msg);
        }
    }
}
