package com.kevinalbs.puzzle;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Kevin on 2/12/2016.
 */
public class PuzzleInputListener implements GestureDetector.GestureListener, InputProcessor {
    private boolean westDown, eastDown, northDown, southDown;
    private boolean swipeEast, swipeWest, swipeNorth, swipeSouth;
    private static float swipeThreshold = 50;

    public boolean isIndicatingEast() {
        return eastDown || swipeEast;
    }

    public boolean isIndicatingWest() {
        return westDown || swipeWest;
    }

    public boolean isIndicatingSouth() {
        return southDown || swipeSouth;
    }

    public boolean isIndicatingNorth() {
        return northDown || swipeNorth;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        float difference = Math.abs(velocityX) - Math.abs(velocityY);
        if (Math.abs(difference) < swipeThreshold) return false;
        if (difference > 0) {
            // The major axis is x.
            if (velocityX > 0) swipeEast = true;
            else swipeWest = true;
        } else {
            // The major axis is y.
            if (velocityY > 0) swipeSouth = true;
            else swipeNorth = true;

        }
        return true;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.RIGHT) {
            eastDown = true;
            return true;
        }
        if (keycode == Input.Keys.LEFT) {
            westDown = true;
            return true;
        }
        if (keycode == Input.Keys.DOWN) {
            southDown = true;
            return true;
        }
        if (keycode == Input.Keys.UP) {
            northDown = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.RIGHT) {
            eastDown = false;
            return true;
        }
        if (keycode == Input.Keys.LEFT) {
            westDown = false;
            return true;
        }
        if (keycode == Input.Keys.DOWN) {
            southDown = false;
            return true;
        }
        if (keycode == Input.Keys.UP) {
            northDown = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public void clear() {
        swipeEast = swipeNorth = swipeWest = swipeSouth = false;
        eastDown = northDown = westDown = southDown = false;
    }
}
