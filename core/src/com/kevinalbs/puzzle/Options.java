package com.kevinalbs.puzzle;

/**
 * Created by Kevin on 1/29/2016.
 */
public class Options {
    private boolean isDebugging = true;
    public boolean isDebugging() {
        return isDebugging;
    }
    public Options(String[] args) {
        for (int i = 0; i < args.length; i++) {
            System.out.println(args[i]);
        }
    }
    public Options() {}
}
