package com.kevinalbs.puzzle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;

import com.kevinalbs.puzzle.Board.Direction;
import com.kevinalbs.puzzle.Tile.Type;


/**
 * Created by Kevin on 2/8/2016.
 * This class manages the rendering of the board and pieces.
 * It assumes that the viewport size is unchanging.
 */
public class DisplayBoard {
    private PuzzleGame game;
    private Board board;

    private static final float SCREEN_WIDTH = Gdx.graphics.getWidth();
    private static final float SCREEN_HEIGHT = Gdx.graphics.getHeight();

    // This is the padding on one side. All four sides have equal padding.
    // Note, because walls are drawn inward, the padding is likely unnecessary.
    private static final float PADDING = 0;

    // This refers to the availableDiameterSizes array.
    private static final int PREFERRED_DIAMETER_INDEX = 1;

    // This is the preferred percentage of the displayable screen (without padding)
    // filled by the board. This is only used to scale up the board if higher sizes are available.
    private static final float PREFERRED_MIN_RATIO_FILLED = .3f;

    // The following corresponds to available images to render piece sizes. This must be in
    // increasing sorted order and have corresponding pieces/<x>/piece#.png files.
    private float[] availableDiameterSizes = {20, 32, 64, 128};
    private float borderSize = 2;

    // If there is extra room, the board is centered. The offsets say how much space from the
    // north/west to start the board.
    private float northCenterOffset = 0;
    private float westCenterOffset = 0;

    private int nHorizontal;
    private int nVertical;

    private float diameter;
    private Texture wallTexture;
    private Array<Texture> pieceTextures;

    public DisplayBoard(PuzzleGame game, Batch batch, Board board) {
        nHorizontal = board.getWidth();
        nVertical = board.getHeight();
        this.game = game;
        this.board = board;
        this.wallTexture = new Texture("wall.png");
        this.pieceTextures = new Array<Texture>();
        this.pieceTextures.add(new Texture("pieces/32/2.png"));
        // TODO If the screen size is large enough, make the border thicker.
        // If the preferred size cannot fit in either width/height of screen, scale down.
        float maxDiameterHorizontal =
                (SCREEN_WIDTH - (PADDING * 2) - (nHorizontal + 1) * borderSize) / nHorizontal;
        float maxDiameterVertical =
                (SCREEN_HEIGHT - (PADDING * 2) - (nVertical + 1) * borderSize) / nVertical;
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
        }

        diameter = availableDiameterSizes[chosenIndex];
        System.out.println("Choosing to use diameter size of " + diameter);

        // Compute the centering offsets.
        float boardWidth = (nHorizontal + 1) * borderSize + nHorizontal * diameter;
        float boardHeight = (nVertical + 1) * borderSize + nVertical * diameter;
        westCenterOffset = (SCREEN_WIDTH - boardWidth) / 2;
        northCenterOffset = (SCREEN_HEIGHT - boardHeight) / 2;
    }

    public void render(Batch batch) {
        batch.begin();
        for (int i = 0; i < nVertical; i++) {
            for (int j = 0; j < nHorizontal; j++) {
                this.drawTile(batch, board.tileAt(i,j));
            }
        }

        for (Piece piece: board.getPieces()) {
            drawPiece(batch, piece);
        }
        batch.end();
    }

    private void drawPiece(Batch batch, Piece piece) {
        batch.draw(pieceTextures.get(0), getGridX(piece.j()), getGridY(piece.i()));
    }

    // TODO: see if we can cache the image for the background since it is likely to be static.
    // See framebuffer.
    private void drawTile(Batch batch, Tile tile) {
        if (tile.type() == Type.WALL) {
            if (tile.isWallHorizontal()) {
                Tile below = board.tileBelow(tile);
                if (below == null || below.isOutside()) {
                    // Inside is up.
                    drawWall(batch, tile.i(), tile.j(), Direction.NORTH);
                } else {
                    // Inside is down.
                    drawWall(batch, tile.i(), tile.j(), Direction.SOUTH);
                }
            }
            else if (tile.isWallVertical()) {
                Tile right = board.tileRightOf(tile);
                if (right == null || right.isOutside()) {
                    // Inside is left.
                    drawWall(batch, tile.i(), tile.j(), Direction.WEST);
                } else {
                    // Inside is right.
                    drawWall(batch, tile.i(), tile.j(), Direction.EAST);
                }
            } else if (tile.isWallCorner()) {
                Tile encased = board.tileEncased(tile);
                if (!encased.isOutside()) {
                    // Skip drawing this tile.
                    return;
                }
                // We should draw this corner.
                if (tile.wallNorth()) drawWall(batch, tile.i(), tile.j(), Direction.SOUTH);
                else drawWall(batch, tile.i(), tile.j(), Direction.NORTH);

                if (tile.wallWest()) drawWall(batch, tile.i(), tile.j(), Direction.EAST);
                else drawWall(batch, tile.i(), tile.j(), Direction.WEST);
            }
        }
    }

    private void drawWall(Batch batch, int i, int j, Direction direction) {
        float x = 0, y = 0, width = 0, height = 0;
        switch (direction) {
            case NORTH:
                x = getGridXWithoutBorder(j);
                y = getGridYWithoutBorder(i) + diameter + borderSize;
                width = diameter + 2 * borderSize;
                height = borderSize;
                break;
            case SOUTH:
                x = getGridXWithoutBorder(j);
                y = getGridYWithoutBorder(i);
                width = diameter + 2 * borderSize;
                height = borderSize;
                break;
            case WEST:
                x = getGridXWithoutBorder(j);
                y = getGridYWithoutBorder(i);
                width = borderSize;
                height = diameter + 2 * borderSize;
                break;
            case EAST:
                x = getGridXWithoutBorder(j) + diameter + borderSize;
                y = getGridYWithoutBorder(i);
                width = borderSize;
                height = diameter + 2 * borderSize;
                break;
        }
        batch.draw(wallTexture, x, y, width, height);
    }

    private float getGridX(int i) {
        return westCenterOffset + PADDING + diameter * i + borderSize * (i + 1);
    }

    private float getGridXWithoutBorder(int i) {
        return westCenterOffset + PADDING + diameter * i + borderSize * i;
    }

    // Y coordinate is flipped.
    private float getGridY(int i) {
        return SCREEN_HEIGHT - (northCenterOffset + PADDING + diameter * i + borderSize * (i + 1));
    }

    private float getGridYWithoutBorder(int i) {
        return SCREEN_HEIGHT - (northCenterOffset + PADDING + diameter * i + borderSize * i);
    }
}
