package com.kevinalbs.puzzle;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;

public class PuzzleGame extends ApplicationAdapter {
    ShapeRenderer shapeRenderer;
    Texture piece2, wall;
	SpriteBatch batch;
    OrthographicCamera camera;
    Options options;
    DisplayBoard displayBoard;

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
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        piece2 = new Texture("piece-2.png");
        wall = new Texture("wall.png");
		batch = new SpriteBatch();


        if (options.isDebugging()) {
            BoardReader reader = new BoardReader();
            Board board = reader.getBoard(1);
            displayBoard = new DisplayBoard(this, batch, board);
            System.out.println(board);
            System.out.println("Moving west");
            board.move(Board.Direction.WEST);
            System.out.println(board);
            System.out.println("Moving north");
            BoardChange change = board.move(Board.Direction.NORTH);
            for (Piece piece: change.piecesRemovedAfter()) {
                System.out.println("Removed piece " + piece.toString());
            }
            System.out.println(board);
            System.out.println("Undo");
            board.undo();
            System.out.println(board);
        }
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(.957f, .957f, .957f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        batch.setProjectionMatrix(camera.combined);
        displayBoard.render(batch);



	}
}
