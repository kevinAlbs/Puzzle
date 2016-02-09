package com.kevinalbs.puzzle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

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
    private static final float PADDING = Math.min(
            SCREEN_WIDTH / 10,
            SCREEN_HEIGHT / 10);

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
    private Texture wall;
    private Array<Texture> pieces;

    public DisplayBoard(PuzzleGame game, Batch batch, Board board) {
        nHorizontal = board.getWidth();
        nVertical = board.getHeight();
        this.game = game;
        this.board = board;
        this.wall = new Texture("wall.png");
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
    }

    public void render(Batch batch) {
        batch.begin();
        // Draw grid lines.
        for (int i = 0; i <= nHorizontal; i++) {
            // Vertical lines.
            batch.draw(
                    this.wall,
                    getGridXWithoutBorder(i),
                    getGridYWithoutBorder(0),
                    borderSize,
                    getGridY(nVertical) - getGridY(0));
        }
        for (int i = 0; i <= nVertical; i++) {
            // Horizontal lines.
            batch.draw(
                    this.wall,
                    getGridXWithoutBorder(0),
                    getGridYWithoutBorder(i),
                    getGridX(nHorizontal) - getGridX(0),
                    borderSize);
        }
        batch.end();
    }

    private float getGridX(int i) {
        return PADDING + diameter * i + borderSize * (i + 1);
    }

    private float getGridXWithoutBorder(int i) {
        return PADDING + diameter * i + borderSize * i;
    }

    private float getGridY(int i) {
        return PADDING + diameter * i + borderSize * (i + 1);
    }
    private float getGridYWithoutBorder(int i) {
        return PADDING + diameter * i + borderSize * i;
    }
}
