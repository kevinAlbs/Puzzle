package com.kevinalbs.puzzle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Interpolation;

import com.kevinalbs.puzzle.Board.Direction;
import com.kevinalbs.puzzle.Tile.Type;

import java.util.LinkedList;


/**
 * Created by Kevin on 2/8/2016.
 * This class manages the rendering of the board and pieces.
 * It assumes that the viewport size is unchanging.
 * All drawing is done assuming y coordinates are from top to bottom (flipped from standard).
 */
public class DisplayBoard {
    private PuzzleGame game;
    private Board board;
    private enum State { ADDING, REMOVING, INTERPOLATING, IDLE };

    // TODO: determine min screen dimensions based on largest board needed.
    // Then in screen class, scale the camera if necessary.
    public static final int MIN_SCREEN_WIDTH = 0;
    public static final int MIN_SCREEN_HEIGHT = 0;

    private static final float MOVE_INTERPOLATION_TIME_PER_SPACE = .1f;
    private static float SCREEN_WIDTH;
    private static float SCREEN_HEIGHT;

    // This is the padding on one side. All four sides have equal padding.
    // Note, because walls are drawn inward, the padding is likely unnecessary.
    private static final float PADDING = 0;

    // This refers to the availableDiameterSizes array.
    private static final int PREFERRED_DIAMETER_INDEX = 1;

    // This is the preferred percentage of the displayable screen (without padding)
    // filled by the board. This is only used to scale up the board if higher sizes are available.
    private static final float PREFERRED_MIN_RATIO_FILLED = .9f;

    // The following corresponds to available images to render piece sizes. This must be in
    // increasing sorted order and have corresponding pieces/<x>/piece#.png files.
    private int[] availableDiameterSizes = {20, 32, 64, 96, 128};
    private int borderSize = 2;

    // If there is extra room, the board is centered. The offsets say how much space from the
    // north/west to start the board.
    private float northCenterOffset = 0;
    private float westCenterOffset = 0;

    private int diameter;
    private Texture wallTexture, insideTexture;
    private Array<Texture> pieceTextures;
    private Array<Texture> holeTextures;
    private LinkedList<Piece> pieces;

    private State state = State.IDLE;

    private Interpolator iInterpolator, jInterpolator;

    public DisplayBoard(PuzzleGame game, Board board, float viewportWidth, float viewportHeight) {
        this.game = game;
        this.board = board;
        this.SCREEN_WIDTH = viewportWidth;
        this.SCREEN_HEIGHT = viewportHeight;
        this.pieces = new LinkedList<Piece>();
        board.getPieces(this.pieces);
        this.wallTexture = new Texture("wall.png");
        this.insideTexture = new Texture("inside.png");
        this.holeTextures = new Array<Texture>();
        this.pieceTextures = new Array<Texture>();

        determineDimensions();

        for (int i = 1; i <= Board.MAX_PIECES; i++) {
            // TODO: use repository loader to prevent reloading.
            // Load appropriate piece textures based on screen size.
            this.pieceTextures.add(new Texture("pieces/" + diameter + "/" + i + ".png"));
            this.holeTextures.add(new Texture("holes/" + i + ".png"));
        }

    }

    public void determineDimensions() {
        int numRows = board.getNumRows();
        int numCols = board.getNumCols();

        // TODO If the screen size is large enough, make the border thicker.
        if (SCREEN_WIDTH > 800 && SCREEN_HEIGHT > 800) {
            borderSize = 4;
        }
        // If the preferred size cannot fit in either width/height of screen, scale down.
        float maxDiameterHorizontal =
                (SCREEN_WIDTH - (PADDING * 2) - (numCols + 1) * borderSize) / numCols;
        float maxDiameterVertical =
                (SCREEN_HEIGHT - (PADDING * 2) - (numRows + 1) * borderSize) / numRows;
        float maxDiameter = Math.min(maxDiameterHorizontal, maxDiameterVertical);
        int chosenIndex = -1;
        for (int i = PREFERRED_DIAMETER_INDEX; i >= 0; i--) {
            if (availableDiameterSizes[i] <= maxDiameter) {
                chosenIndex = i;
                break;
            }
        }
        if (chosenIndex == -1) {
            throw new IllegalArgumentException("Cannot scale down board enough to display.");
        }

        // Now, attempt to increase the chosenIndex if we are below the preferred percentage.
        while (chosenIndex < availableDiameterSizes.length - 1
                && availableDiameterSizes[chosenIndex] / maxDiameter < PREFERRED_MIN_RATIO_FILLED) {
            if (availableDiameterSizes[chosenIndex + 1] < maxDiameter) chosenIndex++;
            else break;
        }

        diameter = availableDiameterSizes[chosenIndex];
        System.out.println("Choosing to use diameter size of " + diameter);

        // Compute the centering offsets.
        float boardWidth = (numCols + 1) * borderSize + numCols * diameter;
        float boardHeight = (numRows + 1) * borderSize + numRows * diameter;
        westCenterOffset = (SCREEN_WIDTH - boardWidth) / 2;
        northCenterOffset = (SCREEN_HEIGHT - boardHeight) / 2;
    }

    public void render(Batch batch) {
        batch.begin();
        for (int i = 0; i < board.getNumRows(); i++) {
            for (int j = 0; j < board.getNumCols(); j++) {
                this.drawTile(batch, board.tileAt(i,j));
            }
        }

        for (Piece piece: this.pieces) {
            drawPiece(batch, piece);
        }
        batch.end();

        if (this.state != State.IDLE) {
            tickAnimations();
        }
    }

    private void tickAnimations() {
        if (iInterpolator.isFinished() && jInterpolator.isFinished()) {
            // Replace our internal pieces.
            this.state = State.IDLE;
            board.getPieces(this.pieces);
            Gdx.graphics.requestRendering();
        }

        if (this.state == State.INTERPOLATING) {
            // Update move interpolators.
            iInterpolator.tick();
            jInterpolator.tick();
        }
    }

    public void applyChange(BoardChange change) {
        if (!isIdle()) {
            throw new IllegalStateException("Cannot interpolate new change, already interpolating");
        }
        Direction direction = change.direction();
        int numSpaces = change.numSpaces();
        int iDiff = Board.iIncrement(direction) * numSpaces;
        int jDiff = Board.jIncrement(direction) * numSpaces;

        float interpolatorTime = MOVE_INTERPOLATION_TIME_PER_SPACE * Math.min(numSpaces, 4);
        iInterpolator = new Interpolator(
                interpolatorTime, 0, iDiff * (diameter + borderSize));
        jInterpolator = new Interpolator(
                interpolatorTime, 0, jDiff * (diameter + borderSize));

        this.state = State.INTERPOLATING;
    }

    public boolean isIdle() {
        return state == State.IDLE;
    }

    private void drawPiece(Batch batch, Piece piece) {
        Texture pieceTexture = pieceTextures.get(piece.pieceNumber() - 1);
        float x = getGridX(piece.j());
        float y = getGridY(piece.i());
        if (this.state == State.INTERPOLATING) {
            x += jInterpolator.getValue();
            y += iInterpolator.getValue();
        }
        batch.draw(pieceTexture, x, y);
    }

    // TODO: see if we can cache the image for the background since it is likely to be static.
    // See framebuffer.
    private void drawTile(Batch batch, Tile tile) {
        if (tile.type() == Type.WALL) {
            if (tile.isWallHorizontal()) {
                Tile below = board.tileBelow(tile);
                if (below == null || below.isOutside()) {
                    // Inside is up.
                    drawWall(batch, tile, Direction.NORTH);
                } else {
                    // Inside is down.
                    drawWall(batch, tile, Direction.SOUTH);
                }
            }
            else if (tile.isWallVertical()) {
                Tile right = board.tileRightOf(tile);
                if (right == null || right.isOutside()) {
                    // Inside is left.
                    drawWall(batch, tile, Direction.WEST);
                } else {
                    // Inside is right.
                    drawWall(batch, tile, Direction.EAST);
                }
            } else if (tile.isWallCorner()) {
                Tile encased = board.tileEncased(tile);
                if (!encased.isOutside()) {
                    // Draw the tiny square.
                    float x = getGridX(tile.j());
                    float y = getGridY(tile.i());
                    if (tile.wallSouth()) {
                        y += diameter - borderSize;
                    }
                    if (tile.wallEast()) {
                        x += diameter - borderSize;
                    }
                    batch.draw(wallTexture, x, y, borderSize, borderSize);
                } else {
                    // We should draw this corner.
                    if (tile.wallNorth()) drawWall(batch, tile, Direction.SOUTH);
                    else drawWall(batch, tile, Direction.NORTH);

                    if (tile.wallWest()) drawWall(batch, tile, Direction.EAST);
                    else drawWall(batch, tile, Direction.WEST);
                }
            }
        }
        else if (tile.type() == Type.HOLE) {
            Texture holeTexture = holeTextures.get(tile.holeNumber() - 1);
            batch.draw(holeTexture,
                    getGridX(tile.j()),
                    getGridY(tile.i()),
                    diameter,
                    diameter
            );
        } else if (tile.type() == Type.BLOCK) {
            batch.draw(wallTexture,
                    getGridX(tile.j()),
                    getGridY(tile.i()),
                    diameter,
                    diameter
            );
        } else if (tile.type() == Type.EMPTY && !tile.isOutside()) {
            batch.draw(insideTexture,
                    getGridX(tile.j()),
                    getGridY(tile.i()),
                    diameter,
                    diameter
            );
        }
    }

    private void drawWall(Batch batch, Tile tile, Direction direction) {
        int i = tile.i();
        int j = tile.j();
        float x = 0, y = 0, width = 0, height = 0;

        if (direction == Direction.NORTH || direction == Direction.SOUTH) {
            x = getGridX(j);
            y = getGridY(i);
            width = diameter;
            height = borderSize;

            if (direction == Direction.SOUTH) {
                y += diameter - borderSize;
            }
            if (tile.wallWest()) {
                x -= borderSize;
                width += borderSize;
            }
            if (tile.wallEast()) {
                width += borderSize;
            }
        } else if (direction == Direction.WEST || direction == Direction.EAST) {
            x = getGridX(j);
            y = getGridY(i);
            width = borderSize;
            height = diameter;

            if (direction == Direction.EAST) {
                x += diameter - borderSize;
            }
            if (tile.wallNorth()) {
                y -= borderSize;
                height += borderSize;
            }
            if (tile.wallSouth()) {
                height += borderSize;
            }
        }
        batch.draw(wallTexture, x, y, width, height);
    }

    private float getGridX(int i) {
        return westCenterOffset + PADDING + diameter * i + borderSize * (i + 1);
    }

    private float getGridXWithoutBorder(int i) {
        return westCenterOffset + PADDING + diameter * i + borderSize * i;
    }

    private float getGridY(int i) {
        return northCenterOffset + PADDING + diameter * i + borderSize * (i + 1);
    }

    private float getGridYWithoutBorder(int i) {
        return northCenterOffset + PADDING + diameter * i + borderSize * i;
    }
}
