package com.kevinalbs.puzzle;

import com.badlogic.gdx.utils.Array;

import java.util.LinkedList;

/**
 * Created by Kevin on 1/30/2016.
 */
public class Board {
    private int boardWidth, boardHeight;
    private Array<Tile> tiles;
    private LinkedList<Piece> pieces;

    public Board(int width, Array<Tile> tiles, LinkedList<Piece> pieces) {
        this.tiles = tiles;
        this.pieces = pieces;
        this.boardWidth = width;
        this.boardHeight = tiles.size / boardWidth;
    }

    public void printDebug() {
        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                System.out.println(tiles.get(i * boardWidth + j));
            }
        }
    }
}
