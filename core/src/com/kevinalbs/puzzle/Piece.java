package com.kevinalbs.puzzle;

/**
 * Created by Kevin on 1/30/2016.
 */
public class Piece {
    int i, j, pieceNumber;
    public Piece(int i, int j, int pieceNumber) {
        this.i = i;
        this.j = j;
        this.pieceNumber = pieceNumber;
    }

    public String toString() {
        return this.pieceNumber + "";
    }
}
