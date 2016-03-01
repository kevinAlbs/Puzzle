package com.kevinalbs.puzzle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

/**
 * Created by Kevin on 2/29/2016.
 */
public class PuzzleButton extends TextButton {
    private boolean justPressed = false;
    private boolean pressedLock = false;

    private PuzzleButton(String text, TextButton.TextButtonStyle style) {
        super(text, style);
    }

    public void act(float delta) {
        super.act(delta);
        if (this.isPressed()) {
            if (!pressedLock) justPressed = true;
            else justPressed = false;
            pressedLock = true;
        } else {
            pressedLock = false;
            justPressed = false;
        }
        setChecked(false);
    }

    public boolean isJustPressed() {
        return justPressed;
    }

    public static PuzzleButton make(String text, int fontSize) {
        ResourceLoader loader = ResourceLoader.get();
        BitmapFont buttonFont = loader.getFont("overpass.ttf", fontSize);

        Texture buttonTexture = loader.getTexture("buttons/default.9.png");
        Texture buttonOverTexture = loader.getTexture("buttons/over.9.png");

        NinePatch buttonNinePatch = new NinePatch(buttonTexture, 8, 8, 8, 8);
        NinePatchDrawable buttonDrawable = new NinePatchDrawable(buttonNinePatch);

        NinePatch buttonOverNinePatch = new NinePatch(buttonOverTexture, 8, 8, 8, 8);
        NinePatchDrawable buttonOverDrawable = new NinePatchDrawable(buttonOverNinePatch);

        TextButton.TextButtonStyle buttonStyle =
                new TextButton.TextButtonStyle(buttonDrawable,
                        buttonOverDrawable,
                        buttonDrawable,
                        buttonFont);
        buttonStyle.over = buttonOverDrawable;
        buttonStyle.fontColor = new Color(0,0,0,1);

        return new PuzzleButton(text, buttonStyle);
    }
}
