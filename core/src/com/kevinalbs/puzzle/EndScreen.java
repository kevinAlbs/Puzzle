package com.kevinalbs.puzzle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import javax.annotation.Resource;

/**
 * Created by Kevin on 3/1/2016.
 */
public class EndScreen extends ScreenAdapter {
    private Stage stage;
    private PuzzleGame game;
    private PuzzleButton retry;

    public EndScreen(PuzzleGame game, int numLevels) {
        this.game = game;
        stage = new Stage(new ScreenViewport(), game.batch);
        Gdx.input.setInputProcessor(stage);
        Table table = new Table();
        stage.addActor(table);
        table.setFillParent(true);
        ResourceLoader loader = ResourceLoader.get();
        BitmapFont font = loader.getFont("overpass.ttf", 24);
        Label.LabelStyle style =new Label.LabelStyle();
        style.fontColor = new Color(0,0,0,1);
        style.font = font;

        Label label1 = new Label("You've completed all " + numLevels + " levels.", style);
        Label label2 = new Label("New levels come out weekly.", style);
        retry = PuzzleButton.make("Return", 32, 20, 10);
        table.padLeft(10).padTop(10).add(label1).expandX();
        table.row();
        table.add(label2);
        table.row();
        table.add(retry).padTop(10);
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(.957f, .957f, .957f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();

        if (retry.isJustPressed()) {
            //Gdx.files.local("currentLevel.txt").delete();
            game.setScreen(new LevelScreen(game, 0));
        }
    }
}
