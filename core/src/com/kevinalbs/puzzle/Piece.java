package com.kevinalbs.puzzle;

/**
 * Created by Kevin on 1/30/2016.
 */
public class Piece {
    public int i, j;
    private int pieceNumber;

    public Piece(int i, int j, int pieceNumber) {
        this.i = i;
        this.j = j;
        this.pieceNumber = pieceNumber;
    }

    public int i() { return i; }
    public int j() { return j; }
    public int pieceNumber() {return pieceNumber; }
    public Piece move(int iDiff, int jDiff) {
        return new Piece(i + iDiff, j + jDiff, pieceNumber);
    }

    public String toString() {
        return this.pieceNumber + "";
    }
}
