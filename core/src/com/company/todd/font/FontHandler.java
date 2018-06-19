package com.company.todd.font;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import com.company.todd.launcher.ToddEthottGame;

import java.io.File;

public class FontHandler {
    private static GlyphLayout layout = new GlyphLayout();

    public static BitmapFont generateFont(final String fileName, final int size, final Color color) {
        StringBuilder characters = new StringBuilder();
        for (char i = 21; i <= 127; i++) characters.append(i); // цифры, весь английский и служебные символы
        for (char i = 'А'; i <= 'я'; i++) characters.append(i); // русские символы

        FreeTypeFontGenerator generator =
                new FreeTypeFontGenerator(Gdx.files.internal(
                        ToddEthottGame.FONTS_FOLDER + File.separator + fileName
                ));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter =
                new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.characters = characters.toString();
        parameter.size = size;
        parameter.color = color;

        BitmapFont font;
        font = generator.generateFont(parameter);
        generator.dispose();

        return font;
    }

    public static FontSize getTextSize(BitmapFont font, String text) {
        layout.setText(font, text);
        return new FontSize(layout.width, layout.height);
    }

    public static class FontSize {
        public float width, height;

        FontSize(float width, float height) {
            this.width = width;
            this.height = height;
        }
    }
}
