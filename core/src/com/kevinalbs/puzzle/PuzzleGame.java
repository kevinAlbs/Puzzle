package com.kevinalbs.puzzle;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class PuzzleGame extends ApplicationAdapter {
    ShapeRenderer shapeRenderer;
    Texture piece2;
	SpriteBatch batch;
    OrthographicCamera camera;
    Options options;

	public PuzzleGame() {
        super();
		options = new Options();
	}
    public PuzzleGame(Options options) {
        super();
        this.options = options;
    }
	
	@Override
	public void create () {
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera(400, 400 * Gdx.graphics.getHeight() / Gdx.graphics.getWidth());
        piece2 = new Texture("piece-2.png");
		batch = new SpriteBatch();

        if (options.isDebugging()) {
            BoardReader reader = new BoardReader();
            Board board = reader.getBoard(1);
            board.printDebug();
        }
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(.957f, .957f, .957f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
		batch.begin();
        batch.draw(piece2, 50, 50);
        batch.end();

        int radius = 20;
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(.380f, 1, .592f, 1);
        shapeRenderer.circle(100, 100, radius);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeType.Line);
        shapeRenderer.setColor(.376f, .714f, .357f, 1);
        shapeRenderer.circle(100, 100, radius);
        shapeRenderer.circle(100, 100, radius+1);
        shapeRenderer.end();

	}
}
