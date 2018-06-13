package com.company.todd.font;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import com.company.todd.ToddEthottGame;

import java.io.File;

public class FontGenerator {
    public static BitmapFont generateFont(final String file_name, final int size, final Color color) {
        StringBuilder characters = new StringBuilder();
        for (char i = 21; i <= 127; i++) characters.append(i); // цифры, весь английский и служебные символы
        for (char i = 'А'; i <= 'я'; i++) characters.append(i); // русские символы

        FreeTypeFontGenerator generator =
                new FreeTypeFontGenerator(Gdx.files.internal(
                        ToddEthottGame.FONTS_FOLDER + File.separator + file_name
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
}
