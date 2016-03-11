package com.kevinalbs.puzzle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PuzzleGame extends Game {
	SpriteBatch batch;
    Options options;

	public PuzzleGame(FontLoader fontLoader) {
        this(fontLoader, new Options());
	}

    public PuzzleGame(FontLoader fontLoader, Options options) {
        super();
		ResourceLoader.init(fontLoader);
        this.options = options;
    }
	
	@Override
	public void create () {
        batch = new SpriteBatch();
        this.setScreen(new LevelScreen(this));
	}

	@Override
	public void render () {
        super.render();
	}
}
