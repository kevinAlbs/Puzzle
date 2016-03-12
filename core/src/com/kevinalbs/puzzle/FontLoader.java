package com.kevinalbs.puzzle;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Because HTML build does not support generating different sized fonts on the fly,
 * we'll use another implementation for the HTML build which just selects from a fixed
 * set of pregenerated fonts.
 */
public interface FontLoader {
    public BitmapFont getFont(String filename, int size);
}
