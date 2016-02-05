package com.kevinalbs.puzzle;

import java.util.LinkedList;
import com.kevinalbs.puzzle.Board.Direction;

/**
 * Created by Kevin on 2/4/2016.
 * BoardChange represents the change occurred at the end of a move.
 */
public class BoardChange {
    // Only applicable for an undo, represents the pieces added before reversal of movement.
    private LinkedList<Piece> piecesAddedBefore;
    // Represents the pieces removed after the movement.
    private LinkedList<Piece> piecesRemovedAfter;
    private Direction direction;
    private int numSpaces;
    public BoardChange(LinkedList<Piece> piecesAdded,
                       LinkedList<Piece> piecesRemoved,
                       Direction direction,
                       int numSpaces) {
        this.piecesAddedBefore = piecesAdded;
        this.piecesRemovedAfter = piecesRemoved;
        this.direction = direction;
        this.numSpaces = numSpaces;
    }

    public LinkedList<Piece> piecesAddedBefore() { return piecesAddedBefore; };
    public LinkedList<Piece> piecesRemovedAfter() { return piecesRemovedAfter; }
    public Direction direction() { return direction; }
    public int numSpaces() { return numSpaces; }

    public BoardChange invert() {
        Direction reverse = null;
        if (direction == Direction.NORTH) reverse = Direction.SOUTH;
        else if (direction == Direction.SOUTH) reverse = Direction.NORTH;
        else if (direction == Direction.WEST) reverse = Direction.EAST;
        else if (direction == Direction.EAST) reverse = Direction.WEST;
        return new BoardChange(piecesRemovedAfter, piecesAddedBefore, reverse, numSpaces);
    }
}
