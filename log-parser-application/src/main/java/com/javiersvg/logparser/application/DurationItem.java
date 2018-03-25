package com.javiersvg.logparser.application;

import java.time.Duration;

public enum DurationItem {
    HOURLY("hourly", Duration.ofHours(1)),
    DAILY("daily", Duration.ofDays(1));

    private String name;
    private Duration duration;

    DurationItem(String name, Duration duration) {
        this.name = name;
        this.duration = duration;
    }

    public Duration getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return name;
    }
}
