package com.kevinalbs.puzzle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;

/**
 * Created by Kevin on 2/15/2016.
 */
public class LevelScreen extends ScreenAdapter {
    private PuzzleGame game;
    private DisplayBoard displayBoard;
    private Board board;
    private PuzzleInputListener inputListener;
    private BoardReader reader;
    private int currentLevel = 0;
    private OrthographicCamera camera;
    private LevelScreenUI ui;

    public LevelScreen(PuzzleGame game, int level) {
        this.game = game;

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // Flip the y-axis.
        camera.setToOrtho(true);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);

        ui = new LevelScreenUI(game);

        reader = new BoardReader();
        loadLevel(level);

        inputListener = new PuzzleInputListener();
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(inputListener);
        multiplexer.addProcessor(new GestureDetector(inputListener));
        multiplexer.addProcessor(ui.getStage());
        Gdx.input.setInputProcessor(multiplexer);

        // Disable continuous rendering until a swipe motion is made.
        Gdx.graphics.setContinuousRendering(false);
        Gdx.graphics.requestRendering();
    }

    private void loadLevel(int level) {
        if (level > reader.getNumBoards() - 1) {
            return;
        }
        currentLevel = level;
        board = reader.getBoard(currentLevel);
        displayBoard = new DisplayBoard(game, board, camera.viewportWidth, camera.viewportHeight);
        ui.refreshLayout(displayBoard.getAvailableVerticalPadding());
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.957f, .957f, .957f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        ui.render(delta);

        camera.update();

        BoardChange change = null;
        Board.Direction direction = null;

        if (inputListener.isIndicatingEast()) {
            direction = Board.Direction.EAST;
        } else if (inputListener.isIndicatingWest()) {
            direction = Board.Direction.WEST;
        } else if (inputListener.isIndicatingNorth()) {
            direction = Board.Direction.NORTH;
        } else if (inputListener.isIndicatingSouth()) {
            direction = Board.Direction.SOUTH;
        }

        if (ui.isUndoClicked()) {
            change = board.undo();
            if (change != null) {
                displayBoard.applyChange(change);
            }
            // Request rendering to clear the click event.
            Gdx.graphics.requestRendering();
        }
        else if (inputListener.isLongPressed()) {
            this.loadLevel(currentLevel);
            inputListener.clear();
        }
        else if (direction != null) {
            // A move can only be made once the display board is idle.
            inputListener.clear();
            if (displayBoard.isIdle()) {
                change = board.move(direction);
                if (change != null) {
                    displayBoard.applyChange(change);
                }
            }
        }

        game.batch.setProjectionMatrix(camera.combined);
        displayBoard.render(game.batch);

        if (!displayBoard.isIdle()) {
            Gdx.graphics.requestRendering();
        }

        if (displayBoard.isIdle() && board.isBoardCleared()) {
            loadLevel(currentLevel + 1);
        }
    }
}
