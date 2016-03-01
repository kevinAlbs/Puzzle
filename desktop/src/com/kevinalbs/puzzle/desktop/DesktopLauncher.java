package com.kevinalbs.puzzle.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.kevinalbs.puzzle.Options;
import com.kevinalbs.puzzle.Board;
import com.kevinalbs.puzzle.BoardReader;
import com.kevinalbs.puzzle.PuzzleGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Options options = new Options(arg);
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.resizable = false;
        config.width = 400;
        config.height = 640;
        config.addIcon("./desktop-icon.png", Files.FileType.Internal);

        // TODO: remove before releasing.
        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.maxWidth = 1024;
        settings.maxHeight = 1024;
        TexturePacker.process(settings, "./image-sources", "./images", "game");

        // If no arguments are passed, run as usual.
        new LwjglApplication(new PuzzleGame(new DesktopFontLoader(), options), config);
        return;

	}
}
