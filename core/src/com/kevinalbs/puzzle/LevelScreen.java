package com.kevinalbs.puzzle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Kevin on 2/15/2016.
 */
public class LevelScreen extends ScreenAdapter {
    private PuzzleGame game;
    private DisplayBoard displayBoard;
    private Board board;
    private PuzzleInputListener inputListener;
    private BoardReader reader;
    private int currentLevel = 0, highestLevelObtained = 0;
    private OrthographicCamera camera;
    private LevelScreenUI ui;

    public LevelScreen(PuzzleGame game) {
        this(game, -1);
    }

    public LevelScreen(PuzzleGame game, int level) {
        this.game = game;

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // Flip the y-axis.
        camera.setToOrtho(true);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);

        ui = new LevelScreenUI(game);

        reader = new BoardReader();

        if (level < -1 || level > reader.getNumBoards() - 1) {
            throw new IllegalArgumentException("Level invalid");
        }

        // Check is user has played before.
        FileHandle current = Gdx.files.local("currentLevel.txt");
        if (current.exists()) {
            String content = current.readString();
            int savedLevel = Integer.parseInt(content);
            if (savedLevel > level) {
                highestLevelObtained = savedLevel;
            }
        }

        if (level == -1) {
            level = highestLevelObtained;
        }


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
            this.game.setScreen(new EndScreen(game, currentLevel + 1));
            return;
        }

        // Save the level if it's higher.
        if (level > highestLevelObtained) {
            FileHandle current = Gdx.files.local("currentLevel.txt");
            current.writeString(level + "", false);
        }

        currentLevel = level;
        if (currentLevel > highestLevelObtained) {
            highestLevelObtained = currentLevel;
        }
        board = reader.getBoard(currentLevel);
        displayBoard = new DisplayBoard(game, board, camera.viewportWidth, camera.viewportHeight);
        ui.refreshLayout((int) displayBoard.getVerticalPadding(), currentLevel + 1);
        ui.toggleNextButton(currentLevel < highestLevelObtained);
        ui.togglePrevButton(currentLevel > 0);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.957f, .957f, .957f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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

        if (ui.isAttemptingUndo()) {
            if (!board.isBoardCleared() && displayBoard.isIdle()) {
                change = board.undo();
                if (change != null) {
                    displayBoard.applyChange(change);
                }
            }
        }
        else if (ui.isAttemptingRestart()) {
            this.loadLevel(currentLevel);
        }
        else if (ui.isAttemptingNext()) {
            if (board.isBoardCleared() || highestLevelObtained > currentLevel) {
                this.loadLevel(currentLevel + 1);
            }
        }
        else if (ui.isAttemptingPrev()) {
            this.loadLevel(currentLevel - 1);
        }
        else if (direction != null) {
            // A move can only be made once the display board is idle.
            inputListener.clear();
            if (!board.isBoardCleared() && displayBoard.isIdle()) {
                change = board.move(direction);
                if (change != null) {
                    displayBoard.applyChange(change);
                }
            }
        }

        if (board.isBoardCleared() && displayBoard.isIdle()) {
            ui.toggleNextButton(true);
        }

        game.batch.setProjectionMatrix(camera.combined);
        displayBoard.render(game.batch);

        ui.render(delta);
    }
}
