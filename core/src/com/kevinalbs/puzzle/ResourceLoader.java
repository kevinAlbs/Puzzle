package com.kevinalbs.puzzle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import java.util.HashMap;

/**
 * A simple resource loader.
 * Created by Kevin on 2/25/2016.
 */
public class ResourceLoader {
    private static ResourceLoader instance = null;
    private FontLoader fontLoader;
    private HashMap<String, Texture> textureCache;
    private HashMap<String, BitmapFont> fontCache;

    private ResourceLoader(FontLoader fontLoader) {
        this.fontLoader = fontLoader;
        textureCache = new HashMap<String, Texture>();
        fontCache = new HashMap<String, BitmapFont>();
    }

    public BitmapFont getFont(String filename, int size) {
        String key = filename + size;
        if (fontCache.containsKey(key)) {
            return fontCache.get(key);
        }
        BitmapFont font = fontLoader.getFont(filename, size);
        fontCache.put(key, font);
        return font;
    }

    public Texture getTexture(String filename) {
        if (textureCache.containsKey(filename)) {
            return textureCache.get(filename);
        }
        Texture texture = new Texture(filename);
        textureCache.put(filename, texture);
        return texture;
    }

    public void disposeTexture(String filename) {
        if (!textureCache.containsKey(filename)) {
            return;
        }
        textureCache.get(filename).dispose();
        textureCache.remove(filename);
    }

    // This should only be called on exit of the application.
    public void disposeAll() {

    }

    public static ResourceLoader get() {
        if (ResourceLoader.instance == null) {
            throw new IllegalStateException("Resource loader uninitialized");
        }
        return ResourceLoader.instance;
    }

    public static ResourceLoader init(FontLoader fontLoader) {
        ResourceLoader.instance = new ResourceLoader(fontLoader);
        return ResourceLoader.instance;
    }
}
