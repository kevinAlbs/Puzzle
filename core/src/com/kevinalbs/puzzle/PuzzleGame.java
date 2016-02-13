package com.kevinalbs.puzzle;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureAdapter;
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
    PuzzleInputListener inputListener;

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
        board = reader.getBoard(0);
        displayBoard = new DisplayBoard(this, board);

        inputListener = new PuzzleInputListener();
        Gdx.input.setInputProcessor(new GestureDetector(inputListener));

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
        Direction direction = null;

        if (inputListener.isIndicatingEast()) {
            direction = Direction.EAST;
        } else if (inputListener.isIndicatingWest()) {
            direction = Direction.WEST;
        } else if (inputListener.isIndicatingNorth()) {
            direction = Direction.NORTH;
        } else if (inputListener.isIndicatingSouth()) {
            direction = Direction.SOUTH;
        }

        // A move can only be made once the display board is idle.
        if (direction != null && displayBoard.isIdle()) {
            inputListener.clear();
            change = board.move(direction);
            if (change != null) {
                displayBoard.applyChange(change);
            }
        }


        batch.setProjectionMatrix(camera.combined);
        displayBoard.render(batch);

        if (!displayBoard.isIdle()) {
            Gdx.graphics.requestRendering();
        }
	}
}
