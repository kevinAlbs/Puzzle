package com.kevinalbs.puzzle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;

/**
 * Created by Kevin on 2/12/2016.
 */
public class Interpolator {
    // The time in seconds it takes to interpolate one move.
    private float interpolationTime = 1f;
    private float startValue;
    private float endValue;
    private float clock = 0;
    private boolean finished = false;
    private boolean firstRun = true;
    private Interpolation interpolation;

    public Interpolator(float interpolationTime, float startValue, float endValue) {
        this(interpolationTime, startValue, endValue, Interpolation.linear);
    }

    public Interpolator(float interpolationTime,
                        float startValue,
                        float endValue,
                        Interpolation interpolation) {
        if (interpolationTime == 0) {
            throw new IllegalArgumentException("Interpolation time must be > 0");
        }
        this.interpolationTime = interpolationTime;
        this.interpolation = interpolation;
        this.startValue = startValue;
        this.endValue = endValue;
    }

    public boolean isFinished() { return finished; }

    public void finish() {
        finished = true;
    }

    public void tick() {
        if (finished) return;

        // Because of non-continuous rendering, the first delta time is much too large.
        // One solution is to simply not increment on the first call.
        if (firstRun) {
            firstRun = false;
        } else {
            clock += Gdx.graphics.getDeltaTime();
        }

        if (clock > interpolationTime) {
            finished = true;
            clock = interpolationTime;
        }
    }

    public float getValue() {
        // TODO: use ease out interpolator.
        float t = this.interpolation.apply(clock / interpolationTime);
        return startValue + (endValue - startValue) * t;
    }
}
