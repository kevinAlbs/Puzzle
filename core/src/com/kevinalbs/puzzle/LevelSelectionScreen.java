package com.kevinalbs.puzzle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Created by Kevin on 2/14/2016.
 */

/*
UI Considerations
- Resizable fonts are possible with an extension.
- Do I want to use Scene2d or roll my own buttons? One issue is button state, resizing.
 */
public class LevelSelectionScreen extends ScreenAdapter {
    private PuzzleGame game;
    private Stage stage;
    private Table table;
    private BitmapFont font;
    private OrthographicCamera camera;
    private SpriteBatch batch;

    public LevelSelectionScreen(PuzzleGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        float stageWidth = stage.getWidth();
        float stageHeight = stage.getHeight();
        Gdx.input.setInputProcessor(stage);
        table = new Table();
        table.setFillParent(true);
        //table.setDebug(true);
        stage.addActor(table);

        // For Android use the gdx-freetype extension to get appropriate scaling.
        // HTML does not support this, but fortunately we need only consider one viewport for HTML.
        font = new BitmapFont(Gdx.files.internal("overpass.fnt"));

        Texture defaultBorderTexture = new Texture("buttons/default.png");
        NinePatch defaultNinePatch = new NinePatch(defaultBorderTexture, 8, 8, 8, 8);
        NinePatchDrawable defaultDrawable = new NinePatchDrawable(defaultNinePatch);

        Texture overBorderTexture = new Texture("buttons/over.png");
        NinePatch overNinePatch = new NinePatch(overBorderTexture, 8, 8, 8, 8);
        NinePatchDrawable overDrawable = new NinePatchDrawable(overNinePatch);


        TextButton.TextButtonStyle defaultStyle = new TextButton.TextButtonStyle(defaultDrawable, overDrawable, defaultDrawable, font);
        defaultStyle.fontColor = new Color(0,0,0,1);

        TextButton button = new TextButton("Play", defaultStyle);
        table.padTop(stageHeight / 10).top();
        table.add(button).width(stageWidth * 9 / 10);

        button.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                LevelSelectionScreen.this.game.setScreen(new LevelScreen(LevelSelectionScreen.this.game, 2));
            }
        });

        batch = new SpriteBatch();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);


    }
    @Override
    public void show() {
        System.out.println("Level Selection Screen is being shown");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.957f, .957f, .957f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();

        camera.update();
        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        font.setColor(new Color(0,0,0,1f));
        font.draw(batch, "Puzzle", 100, 100);
        batch.end();
    }
}
