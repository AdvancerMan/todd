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
        if (startTime != 0) {
            workingTime += TimeUtils.nanoTime() - startTime;
        }

        System.out.println(name + " debug time in nanos: " + workingTime);

        workingTime = 0;
        startTime = 0;
    }
}
