package com.company.todd.debug;

import com.badlogic.gdx.utils.TimeUtils;

public class DebugTimer {
    protected long startTime;
    protected long workingTime;
    protected String name;

    public DebugTimer(String name) {
        startTime = 0;
        workingTime = 0;
        this.name = name;
    }

    public DebugTimer() {
        this("unknown");
    }

    public void start() {
        startTime = TimeUtils.nanoTime();
    }

    public void stop() {
        workingTime += TimeUtils.nanoTime() - startTime;
        startTime = 0;
    }

    public void finish() {
        finish(0, "time in nanos");
    }

    public void finishInMs() {
        finish(-6, "time in ms");
    }

    public void finishInSeconds() {
        finish(-9, "time in seconds");
    }

    public void finishInSeconds(int frames) {
        finish(-9, frames, "time in seconds * " + frames + " frames");
    }

    protected void finish(int exp10, String message) {
        finish(exp10, 1, message);
    }

    protected void finish(int exp10, float mul, String message) {
        if (startTime != 0) {
            workingTime += TimeUtils.nanoTime() - startTime;
        }

        System.out.println(name + " " + message + ": " + workingTime * Math.pow(10, exp10) * mul);

        workingTime = 0;
        startTime = 0;
    }
}
