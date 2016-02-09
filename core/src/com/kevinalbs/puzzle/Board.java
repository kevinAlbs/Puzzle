package com.kevinalbs.puzzle;

import com.badlogic.gdx.utils.Array;

import java.util.LinkedList;

/**
 * Created by Kevin on 1/30/2016.
 */
public class Board {
    public enum Direction {NORTH, EAST, SOUTH, WEST};
    private int boardWidth, boardHeight;
    private Array<Tile> tiles;
    private LinkedList<Piece> pieces;
    private LinkedList<BoardChange> boardChanges;
    // Cleared upon every move.
    private LinkedList<BoardChange> undos;

    public Board(int width, Array<Tile> tiles, LinkedList<Piece> pieces) {
        this.tiles = tiles;
        this.pieces = pieces;
        this.boardWidth = width;
        this.boardHeight = tiles.size / boardWidth;
        this.boardChanges = new LinkedList<BoardChange>();
        this.undos = new LinkedList<BoardChange>();
    }

    public int getWidth() { return boardWidth; }

    public int getHeight() { return boardHeight; }

    public Array<Tile> getTiles() {
        return tiles;
    }

    public LinkedList<Piece> getPieces() {
        return pieces;
    }

    // Returns true iff all pieces are removed, meaning the player has won.
    boolean isBoardCleared() {
        return pieces.size() == 0;
    }

    public String toString() {
        String str = "";
        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                Piece piece = pieceAt(i, j);
                if (piece != null) str += piece.toString();
                else str += tileAt(i,j).toString();
            }
            str += "\n";
        }
        return str;
    }

    public boolean inBounds(int i, int j) {
        return !(i < 0 || j < 0 || i >= boardHeight || j >= boardWidth);
    }

    public Tile tileAt(int i, int j) {
        if (!inBounds(i,j)) {
            throw new IllegalArgumentException("Unexpected request for invalid tile location");
        }
        return tiles.get(i * boardWidth + j);
    }

    // Returns null if no piece is at that location.
    public Piece pieceAt(int i, int j) {
        if (!inBounds(i,j)) {
            throw new IllegalArgumentException("Unexpected request for invalid tile location");
        }
        for (Piece piece : this.pieces) {
            if (piece.i() == i && piece.j() == j) return piece;
        }
        return null;
    }

    // Returns 0 if the player cannot move.
    public int getMoveSpaces(Direction direction) {
        // Take the minimum of the path of traversable tiles in the specified direction for each
        // piece. Ignore piece interaction. If a piece blocks another piece from moving, then
        // the piece blocking must surely have had a shorter path by anyway.

        // We can certainly prune some cases, e.g. if direction == left and we have two adjacent
        // left pieces, the left one need not be considered. To keep the code simple I'm ignoring
        // this at the moment. TODO.
        int minSpaces = Integer.MAX_VALUE;
        int iIncrement = iIncrement(direction);
        int jIncrement = jIncrement(direction);
        for (Piece piece: this.pieces) {
            int i = piece.i + iIncrement;
            int j = piece.j + jIncrement;
            int numSpaces = 0;

            if (!inBounds(i, j)) {
                throw new IllegalStateException("Board move check is out of bounds." +
                        "Is the board walled in?");
            }

            Tile tile = tileAt(i, j);
            while (tile.isTraversable()) {
                numSpaces++;
                i += iIncrement;
                j += jIncrement;
                tile = tileAt(i, j);
            }
            if (numSpaces < minSpaces) {
                minSpaces = numSpaces;
            }
        }
        return minSpaces;
    }

    public BoardChange move(Direction direction) {
        clearUndos();
        int numSpaces = getMoveSpaces(direction);
        int iDiff = numSpaces * iIncrement(direction);
        int jDiff = numSpaces * jIncrement(direction);
        LinkedList<Piece> newPieces = new LinkedList<Piece>();
        LinkedList<Piece> piecesRemoved = new LinkedList<Piece>();
        for (Piece piece: pieces) {
            Piece moved = piece.move(iDiff, jDiff);
            Tile tile = tileAt(moved.i, moved.j);
            if (tile.type() == Tile.Type.HOLE && tile.holeNumber() == moved.pieceNumber()) {
                piecesRemoved.add(moved);
            } else {
                newPieces.add(moved);
            }
        }
        pieces = newPieces;
        BoardChange change = new BoardChange(null, piecesRemoved, direction, numSpaces);
        boardChanges.add(change);
        return change;
    }
    // TODO.

    // Returns pieces which were removed in the last move.
    // These pieces will have the (i,j) position of the hole on which they were removed.
    public LinkedList<Piece> getRemoved() {
        return null;
    }

    public BoardChange undo() {
        // Get the most recent move and perform the inverse.
        if (boardChanges.size() == 0) return null;
        BoardChange inverse = boardChanges.removeLast().invert();
        applyBoardChange(inverse);
        undos.add(inverse);
        return inverse;
    }

    public BoardChange redo() {
        if (undos.size() == 0) return null;
        BoardChange inverse = undos.removeLast().invert();
        applyBoardChange(inverse);
        boardChanges.add(inverse);
        return inverse;
    }

    private void applyBoardChange(BoardChange change) {
        // Add the pieces before inverting the movement.
        if (change.piecesAddedBefore() != null) {
            pieces.addAll(change.piecesAddedBefore());
        }
        // Invert the movement.
        int iDiff = change.numSpaces() * iIncrement(change.direction());
        int jDiff = change.numSpaces() * jIncrement(change.direction());
        LinkedList<Piece> newPieces = new LinkedList<Piece>();
        // Move all pieces.
        for(Piece piece : pieces) {
            newPieces.add(piece.move(iDiff, jDiff));
        }
        // Remove any pieces which were removed after movement.
        if (change.piecesRemovedAfter() != null) {
            newPieces.removeAll(change.piecesRemovedAfter());
        }
        pieces = newPieces;
    }

    // Clears linked list of undos.
    private void clearUndos() {
        undos = new LinkedList<BoardChange>();
    }


    public static int iIncrement(Direction direction) {
        return direction == Direction.NORTH ? -1 : direction == Direction.SOUTH ? 1 : 0;
    }

    public static int jIncrement(Direction direction) {
        return direction == Direction.WEST ? -1 : direction == Direction.EAST ? 1 : 0;
    }

}
