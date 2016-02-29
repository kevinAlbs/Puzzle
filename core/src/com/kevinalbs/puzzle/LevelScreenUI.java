package com.kevinalbs.puzzle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Created by Kevin on 2/29/2016.
 */
public class LevelScreenUI {
    private Stage stage;
    private TextButton undoButton, menuButton, restartButton;
    private boolean undoPressed, undoJustPressed;

    public LevelScreenUI (PuzzleGame game) {
        stage = new Stage(new ScreenViewport(), game.batch);
    }

    public Stage getStage() {
        return stage;
    }

    public void refreshLayout(int sizeTop) {
        stage.clear();
        // Set up buttons.
        ResourceLoader loader = ResourceLoader.get();
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        int fontSize = Math.min(14, sizeTop / 2);
        BitmapFont buttonFont = loader.getFont("overpass.ttf", fontSize);
        Texture buttonTexture = loader.getTexture("buttons/default.png");
        Texture buttonOverTexture = loader.getTexture("buttons/over.png");

        NinePatch buttonNinePatch = new NinePatch(buttonTexture, 8, 8, 8, 8);
        NinePatchDrawable buttonDrawable = new NinePatchDrawable(buttonNinePatch);

        NinePatch buttonOverNinePatch = new NinePatch(buttonOverTexture, 8, 8, 8, 8);
        NinePatchDrawable buttonOverDrawable = new NinePatchDrawable(buttonOverNinePatch);

        TextButton.TextButtonStyle defaultStyle =
                new TextButton.TextButtonStyle(buttonDrawable,
                        buttonOverDrawable,
                        buttonDrawable,
                        buttonFont);
        defaultStyle.over = buttonOverDrawable;
        defaultStyle.fontColor = new Color(0,0,0,1);
        menuButton = new TextButton("Menu", defaultStyle);
        undoButton = new TextButton("Undo", defaultStyle);
        restartButton = new TextButton("Restart", defaultStyle);

        table.padTop(3).top().left().add(undoButton).left().expandX().padLeft(3);
        table.add(menuButton).center().expandX();
        table.add(restartButton).right().expandX().padRight(3);
    }

    public void render(float delta) {
        stage.act(delta);
        stage.draw();

        // Clear check states so the button style shows hover.
        undoButton.setChecked(false);
        menuButton.setChecked(false);
        restartButton.setChecked(false);

        if (undoButton.isPressed()) {
            if (!undoPressed) undoJustPressed = true;
            else undoJustPressed = false;
            undoPressed = true;
        } else {
            undoPressed = false;
        }

    }

    public boolean isUndoClicked() {
        return undoJustPressed;
    }
}
