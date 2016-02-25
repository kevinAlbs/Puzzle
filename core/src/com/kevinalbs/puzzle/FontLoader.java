package com.kevinalbs.puzzle;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Created by Kevin on 2/25/2016.
 */
public interface FontLoader {
    public BitmapFont getFont(String filename, int size);
}
