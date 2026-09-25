package com.example.tokenvalidation;

import java.time.Duration;

public interface MetricsRecorder {
    void increment(String metricName);

    void recordDuration(String metricName, Duration duration);
}
