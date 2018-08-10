package com.company.todd.debug;

public class DebugTimer {
    private long startTime;
    private String name;

    public DebugTimer(String name) {
        startTime = 0;
        this.name = name;
    }

    public DebugTimer() {
        this("unknown");
    }

    public void start() {
        startTime = System.currentTimeMillis();
    }

    public void finish() {
        System.out.println(name + " debug time: " + (System.currentTimeMillis() - startTime));
    }
}
