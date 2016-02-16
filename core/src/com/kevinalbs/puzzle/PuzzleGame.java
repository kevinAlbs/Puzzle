package com.kevinalbs.puzzle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PuzzleGame extends Game {
	SpriteBatch batch;
    Options options;

	public PuzzleGame() {
        super();
		options = new Options();
	}

    public PuzzleGame(Options options) {
        super();
        this.options = options;
    }
	
	@Override
	public void create () {
        batch = new SpriteBatch();
        this.setScreen(new LevelSelectionScreen(this));
	}

	@Override
	public void render () {
        super.render();
	}
}
