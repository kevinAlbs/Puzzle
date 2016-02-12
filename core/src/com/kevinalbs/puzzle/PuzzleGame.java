package com.kevinalbs.puzzle;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.kevinalbs.puzzle.Board.Direction;

public class PuzzleGame extends ApplicationAdapter {
	SpriteBatch batch;
    OrthographicCamera camera;
    Options options;
    DisplayBoard displayBoard;
    Board board;

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
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setToOrtho(true);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
		batch = new SpriteBatch();

        BoardReader reader = new BoardReader();
        board = reader.getBoard(1);
        displayBoard = new DisplayBoard(this, board);

        // Disable continuous rendering until a swipe motion is made.
        Gdx.graphics.setContinuousRendering(false);
        Gdx.graphics.requestRendering();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(.957f, .957f, .957f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        BoardChange change = null;
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
             change = board.move(Direction.EAST);
        }

        if (change != null) {
            displayBoard.interpolateChange(change);
        }

        batch.setProjectionMatrix(camera.combined);
        displayBoard.render(batch);

        if (displayBoard.isInterpolating()) {
            Gdx.graphics.requestRendering();
        }
	}
}
