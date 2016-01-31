package com.kevinalbs.puzzle;

/**
 * Created by Kevin on 1/30/2016.
 */
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;
import java.util.LinkedList;

public class BoardReader {
    private static String BOARD_FILE = "boards.json";
    private JsonValue json;
    private HashMap<Integer, Board> boardCache;
    public BoardReader () {
        JsonReader reader = new JsonReader();
        this.json = reader.parse(Gdx.files.internal(BOARD_FILE));
        boardCache = new HashMap<Integer, Board>(10);
    }
    public Board getBoard(int index) {
        if (boardCache.containsKey(index)) {
            return boardCache.get(index);
        }
        if (json.size <= index || index < 0) {
            System.err.println("Board requested is not in bounds.");
            return null;
        }
        String[] lines = json.get(index).get("board").asStringArray();
        int boardWidth = lines[0].length();
        for(String line : lines) {
            if (line.length() != boardWidth) {
                System.err.println("Board has jagged width.");
                return null;
            }
        }
        Array<Character> unparsedTiles = new Array<Character>(true, 64);
        LinkedList<Piece> pieceList = new LinkedList<Piece>();
        parseBoard(lines, unparsedTiles, pieceList);

        Array<Tile> parsedTiles = new Array<Tile>(true, unparsedTiles.size);

        parseTiles(boardWidth, unparsedTiles, parsedTiles);
        return new Board(boardWidth, parsedTiles, pieceList);
    }

    private void parseBoard(String[] lines, Array<Character> unparsedTileList, LinkedList<Piece> pieceList) {
        for(int i = 0; i < lines.length; i++) {
            for(int j = 0; j < lines[i].length(); j++) {
                char character = lines[i].charAt(j);
                int difference = character - '1';
                if (difference >= 0 && difference < 9) {
                    Piece piece = new Piece(i, j, difference + 1);
                    pieceList.push(piece);
                    unparsedTileList.add(' ');
                } else {
                    unparsedTileList.add(character);
                }
            }
        }
    }

    // Fills in the type, wall directions, and hole number if applicable.
    private void parseTiles(int boardWidth, Array<Character> unparsedTiles, Array<Tile> tileList) {
        int boardHeight = unparsedTiles.size / boardWidth;
        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                Tile tile = new Tile();
                char character = unparsedTiles.get(i * boardWidth + j);
                tile.type = getTileType(character);
                tile.i = i;
                tile.j = j;

                if (tile.type == Tile.Type.HOLE) {
                    tile.holeNumber = character - 'A' + 1;
                }

                // The north and west tiles (if they exist) are already initialized.
                // We can use this to determine the wall relations without a second pass.
                if (tile.type == Tile.Type.WALL) {
                    if (j > 0) {
                        Tile west = tileList.get(i * boardWidth + (j - 1));
                        if (west.type == Tile.Type.WALL) {
                            west.wallEast = true;
                            tile.wallWest = true;
                        }
                    }

                    if (i > 0) {
                        Tile north = tileList.get((i - 1) * boardWidth + j);
                        if (north.type == Tile.Type.WALL) {
                            north.wallSouth = true;
                            tile.wallNorth = true;
                        }
                    }
                }

                tileList.add(tile);
            }
        }
        // One final pass is done to check whether empty tiles are inside or outside of the board.
        // Note, this requires the board to be constructed correctly (i.e. without holes, and with
        // walls encompassing a non-empty area.
        //
        // This simply toggles a boolean every time a vertical wall is reached.
        for (int i = 0; i < boardHeight; i++) {
            boolean isOutside = true;
            for (int j = 0; j < boardWidth; j++) {
                Tile tile = tileList.get(i * boardWidth + j);
                if (tile.type == Tile.Type.EMPTY) {
                    tile.isOutside = isOutside;
                }
                if (tile.type == Tile.Type.WALL && tile.wallNorth && tile.wallSouth) {
                    isOutside = !isOutside;
                }
            }
        }

    }

    private Tile.Type getTileType(char character) {
        switch(character) {
            case '-':
            case '+':
            case '|':
                return Tile.Type.WALL;
            case ' ':
                return Tile.Type.EMPTY;
            case '#':
                return Tile.Type.BLOCK;
        }
        int difference = character - 'A';
        if (difference >= 0 && difference < 9) {
            return Tile.Type.HOLE;
        }

        System.err.println("Unrecognized character " + character);
        return null;
    }
}
