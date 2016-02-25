package com.kevinalbs.puzzle.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.kevinalbs.puzzle.FontLoader;

import java.util.HashMap;

/**
 * Created by Kevin on 2/25/2016.
 */
public class DesktopFontLoader implements FontLoader {
    HashMap<String, FreeTypeFontGenerator> generatorCache;
    public DesktopFontLoader() {
        generatorCache = new HashMap<String, FreeTypeFontGenerator>();
    }

    public BitmapFont getFont(String filename, int size) {
        FreeTypeFontGenerator generator;
        if (generatorCache.containsKey(filename)) {
            generator = generatorCache.get(filename);
        }
        generator = new FreeTypeFontGenerator(Gdx.files.internal(filename));
        generatorCache.put(filename, generator);
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        return generator.generateFont(parameter);
    }
}
