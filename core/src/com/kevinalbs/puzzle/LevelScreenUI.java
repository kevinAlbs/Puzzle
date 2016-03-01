package com.kevinalbs.puzzle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Created by Kevin on 2/29/2016.
 */
public class LevelScreenUI {
    private Stage stage, dialogStage;
    private PuzzleButton undoButton, restartButton, nextButton;
    private Table nextAreaTable;
    private Label levelLabel;

    public LevelScreenUI (PuzzleGame game) {
        stage = new Stage(new ScreenViewport(), game.batch);
        dialogStage = new Stage(new ScreenViewport(), new SpriteBatch());
    }

    public Stage getStage() {
        return stage;
    }

    public void refreshLayout(int sizeTop, int levelNum) {
        stage.clear();
        // Set up buttons.
        ResourceLoader loader = ResourceLoader.get();
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        int fontSize = (int)(sizeTop * .5f);
        int verticalSpaceLeft = sizeTop - fontSize;
        int verticalPadding = verticalSpaceLeft / 4;

        BitmapFont font = loader.getFont("overpass.ttf", fontSize);


        restartButton = PuzzleButton.make("Restart", fontSize, verticalPadding * 2, verticalPadding);
        undoButton = PuzzleButton.make("Undo", fontSize, verticalPadding * 2, verticalPadding);
        nextButton = PuzzleButton.make("Next Level", fontSize, verticalPadding * 2, verticalPadding);
        levelLabel = new Label("Level " + levelNum, new Label.LabelStyle(
                loader.getFont("overpass.ttf", fontSize * 3 / 5)
                , new Color(0,0,0,1)));

        table.padTop(verticalPadding).top().left().add(restartButton).left().expandX().padLeft(verticalPadding);
        table.add(levelLabel).expandX();
        table.add(undoButton).right().expandX().padRight(sizeTop / 4);

        nextAreaTable = new Table();
        nextAreaTable.setFillParent(true);
        stage.addActor(nextAreaTable);
        nextAreaTable.bottom();

        nextAreaTable.add(nextButton).bottom().right().expandX().padBottom(verticalPadding).padRight(verticalPadding);
        toggleNextArea(false);
    }

    public void render(float delta) {
        stage.act(delta);
        stage.draw();

//        dialogStage.act(delta);
//        dialogStage.draw();

        if (restartButton.isJustPressed()) {
            showRestartDialog();
        }
    }

    public boolean isAttemptingUndo() {
        return undoButton.isJustPressed();
    }

    public boolean isAttemptingRestart() {
        return restartButton.isJustPressed();
    }

    public boolean isAttemptingNext() {
        return nextButton.isJustPressed();
    }
    
    public void toggleNextArea(boolean show) {
        nextAreaTable.setVisible(show);
    }

    private void showRestartDialog() {
        // TODO. Consider best way to approach showing dialogs.
        ResourceLoader loader = ResourceLoader.get();
        int fontSize = 18;
        PuzzleButton restartButton = PuzzleButton.make("Restart", fontSize);
        PuzzleButton levelSelectButton = PuzzleButton.make("Play Another Level", fontSize);
        PuzzleButton closeButton = PuzzleButton.make("Close menu", fontSize);

        Window.WindowStyle windowStyle = new Window.WindowStyle();
        windowStyle.titleFont = loader.getFont("overpass.ttf", 18);

        Texture dialogTexture = loader.getTexture("image-sources/dialog.9.png");
        NinePatch dialogNinePatch = new NinePatch(dialogTexture, 8, 8, 8, 8);
        NinePatchDrawable dialogDrawable = new NinePatchDrawable(dialogNinePatch);

        windowStyle.background = dialogDrawable;
        Dialog dialog = new Dialog("", windowStyle);
        dialog.setPosition(0,0);
        dialog.setWidth(Gdx.graphics.getWidth());

        dialog.row();
        dialog.add(restartButton).fillX();
        dialog.row();
        dialog.add(levelSelectButton).fillX();
        dialog.row();
        dialog.add(closeButton).fillX();
        dialog.pack();
        // Need skin or roll my own.
        // dialog.text("Test");
        dialog.show(dialogStage);
    }
}
