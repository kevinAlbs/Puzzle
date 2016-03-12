package com.kevinalbs.puzzle.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.kevinalbs.puzzle.FontLoader;
import com.kevinalbs.puzzle.PuzzleGame;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(480, 320);
        }

        @Override
        public ApplicationListener getApplicationListener () {
                return new PuzzleGame(new FontLoader() {
                        @Override
                        public BitmapFont getFont(String filename, int size) {
                                return null;
                        }
                });
        }
}