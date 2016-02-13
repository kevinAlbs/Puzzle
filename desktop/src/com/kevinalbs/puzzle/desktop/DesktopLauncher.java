package com.kevinalbs.puzzle.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.kevinalbs.puzzle.Options;
import com.kevinalbs.puzzle.Board;
import com.kevinalbs.puzzle.BoardReader;
import com.kevinalbs.puzzle.PuzzleGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Options options = new Options(arg);
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.resizable = false;
        config.width = 500;
        config.height = 500;

        // If no arguments are passed, run as usual.
        new LwjglApplication(new PuzzleGame(options), config);
        return;

	}
}
