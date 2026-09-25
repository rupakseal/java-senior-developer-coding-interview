package com.example.tokenvalidation;

import java.time.Duration;

public final class NoOpMetricsRecorder implements MetricsRecorder {
    @Override
    public void increment(String metricName) {
        // The production adapter would forward this to the organization's metrics platform.
    }

    @Override
    public void recordDuration(String metricName, Duration duration) {
        // The production adapter would forward this to the organization's metrics platform.
    }
}
