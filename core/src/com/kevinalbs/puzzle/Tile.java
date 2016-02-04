package com.kevinalbs.puzzle;

/**
 * Created by Kevin on 1/30/2016.
 * Class is immutable.
 */
public class Tile {
    enum Type {EMPTY, HOLE, BLOCK, WALL};

    private Tile() {}

    private Type type;
    // If true, then this is outside of the boundaries of the board.
    // Only applicable to the EMPTY type.
    private boolean isOutside;
    // Only applicable for the WALL type.
    private boolean wallNorth, wallSouth, wallWest, wallEast;
    // Only applicable for the HOLE type.
    private int holeNumber;

    private int i, j;

    public Type type() { return type; }
    public boolean isOutside() { return isOutside; }
    public boolean wallNorth() { return wallNorth; }
    public boolean wallSouth() { return wallSouth; }
    public boolean wallWest() { return wallWest; }
    public boolean wallEast() { return wallEast; }
    public int holeNumber() { return holeNumber; }
    public int i() { return i; }
    public int j() { return j; }

    public boolean isTraversable() {
        return this.type == Type.EMPTY || this.type == Type.HOLE;
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

    // Factory class used to create Tiles.
    public static class Maker {
        public Type type = Type.EMPTY;
        public boolean isOutside = false;
        public boolean wallNorth = false, wallSouth = false, wallWest = false, wallEast = false;
        public int holeNumber = 0;
        public int i = 0, j = 0;

        public Tile make() {
            Tile tile = new Tile();
            tile.type = type;
            tile.holeNumber = this.holeNumber;
            tile.isOutside = this.isOutside;
            tile.wallNorth = this.wallNorth;
            tile.wallSouth = this.wallSouth;
            tile.wallWest = this.wallWest;
            tile.wallEast = this.wallEast;
            return tile;
        }
    }

}
