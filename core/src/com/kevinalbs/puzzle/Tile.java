package com.kevinalbs.puzzle;

/**
 * Created by Kevin on 1/30/2016.
 */
public class Tile {
    enum Type {EMPTY, HOLE, BLOCK, WALL};

    Type type;
    // If true, then this is outside of the boundaries of the board.
    // Only applicable to the EMPTY type.
    boolean isOutside;
    // Only applicable for the WALL type.
    boolean wallNorth, wallSouth, wallWest, wallEast;
    // Only applicable for the HOLE type.
    int holeNumber;

    int i, j;

    public boolean isTraversable() {
        return this.type == Type.EMPTY || this.type == Type.HOLE;
    }

    public String toDebugString() {
        String typeString = "Undefined";
        String extra = "";
        switch(type) {
            case EMPTY:
                typeString = "empty";
                extra = isOutside ? "outside" : "inside";
                break;
            case HOLE:
                typeString = "hole";
                extra = "" + holeNumber;
                break;
            case BLOCK:
                typeString = "block";
                break;
            case WALL:
                typeString = "wall";
                if (wallNorth) extra += "north ";
                if (wallEast) extra += "east ";
                if (wallSouth) extra += "south ";
                if (wallWest) extra += "west ";
                break;
        }
        return "(" + i + "," + j + ")" + " " + typeString + " " + extra;
    }

    public String toString() {
        switch(type) {
            case EMPTY:
                return " ";
            case HOLE:
                return "" + (char)('A' + holeNumber - 1);
            case BLOCK:
                return "#";
            case WALL:
                boolean vertical = wallNorth || wallSouth;
                boolean horizontal = wallWest || wallEast;
                if (horizontal && vertical) return "+";
                if (horizontal) return "-";
                return "|";
        }
        return "?";
    }

}
