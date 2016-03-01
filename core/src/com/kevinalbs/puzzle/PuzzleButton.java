package com.kevinalbs.puzzle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

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
        return PuzzleButton.make(text, fontSize, 0, 0);
    }

    public static PuzzleButton make(String text,
                                    int fontSize,
                                    int horizontalPadding,
                                    int verticalPadding) {
        ResourceLoader loader = ResourceLoader.get();
        BitmapFont buttonFont = loader.getFont("overpass.ttf", fontSize);

        // Create textures.
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(.478f, .478f, .478f,1));
        pixmap.fill();

        Texture buttonTexture = new Texture(pixmap);
        TextureRegion buttonTextureRegion = new TextureRegion(buttonTexture);
        TextureRegionDrawable buttonDrawable = new TextureRegionDrawable(buttonTextureRegion);

        // Create textures.
        Pixmap overPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        overPixmap.setColor(new Color(.682f, .682f, .682f, 1));
        overPixmap.fill();

        Texture buttonOverTexture = new Texture(overPixmap);
        TextureRegion buttonOverTextureRegion = new TextureRegion(buttonOverTexture);
        TextureRegionDrawable buttonOverDrawable = new TextureRegionDrawable(buttonOverTextureRegion);

        TextButton.TextButtonStyle buttonStyle =
                new TextButton.TextButtonStyle(buttonDrawable,
                        buttonOverDrawable,
                        buttonDrawable,
                        buttonFont);
        buttonStyle.over = buttonOverDrawable;
        buttonStyle.fontColor = new Color(1, 1, 1, .9f);

        PuzzleButton button =  new PuzzleButton(text, buttonStyle);
        button.padLeft(horizontalPadding);
        button.padRight(horizontalPadding);
        button.padTop(verticalPadding);
        button.padBottom(verticalPadding);
        return button;
    }
}
