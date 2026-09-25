package com.example.tokenvalidation;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A dependency-free baseline test harness. Candidates may extend it or replace it with JUnit.
 */
public final class StarterTestRunner {
    private int passed;
    private int failed;

    public static void main(String[] args) {
        new StarterTestRunner().run();
    }

    private void run() {
        execute("returns valid decision", this::returnsValidDecision);
        execute("rejects missing token", this::rejectsMissingToken);
        execute("retries downstream failure", this::retriesDownstreamFailure);

        System.out.printf("%nResult: %d passed, %d failed%n", passed, failed);
        if (failed > 0) {
            System.exit(1);
        }
    }

    private void returnsValidDecision() {
        FakeAuditLogger logger = new FakeAuditLogger();
        FakeMetrics metrics = new FakeMetrics();
        TokenValidationService service = service(
                token -> AuthorizationDecision.active("subject-123"), logger, metrics);

        ValidationResult result = service.validate("header.payload.signature", "customer-456");

        check(result.status() == ValidationResult.Status.VALID, "expected VALID");
        check("subject-123".equals(result.subject()), "expected subject");
        check(metrics.count("validation.success") == 1, "expected success metric");
    }

    private void rejectsMissingToken() {
        AtomicInteger calls = new AtomicInteger();
        TokenValidationService service = service(token -> {
            calls.incrementAndGet();
            return AuthorizationDecision.active("never-used");
        }, new FakeAuditLogger(), new FakeMetrics());

        ValidationResult result = service.validate(" ", "customer-456");

        check(result.status() == ValidationResult.Status.INVALID, "expected INVALID");
        check(calls.get() == 0, "downstream should not be called");
    }

    private void retriesDownstreamFailure() {
        AtomicInteger calls = new AtomicInteger();
        TokenValidationService service = service(token -> {
            if (calls.incrementAndGet() < 3) {
                throw new IllegalStateException("temporary failure");
            }
            return AuthorizationDecision.active("subject-123");
        }, new FakeAuditLogger(), new FakeMetrics());

        ValidationResult result = service.validate("header.payload.signature", "customer-456");

        check(result.status() == ValidationResult.Status.VALID, "expected eventual success");
        check(calls.get() == 3, "expected three attempts");
    }

    private TokenValidationService service(
            DownstreamAuthorizationClient downstream,
            FakeAuditLogger logger,
            FakeMetrics metrics) {
        return new TokenValidationService(
                downstream,
                logger,
                metrics,
                Clock.fixed(Instant.parse("2026-01-01T00:00:00Z"), ZoneOffset.UTC));
    }

    private void execute(String name, Runnable test) {
        try {
            test.run();
            passed++;
            System.out.println("PASS " + name);
        } catch (Throwable failure) {
            failed++;
            System.err.println("FAIL " + name + ": " + failure.getMessage());
        }
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    static final class FakeAuditLogger implements AuditLogger {
        final List<String> entries = new ArrayList<>();

        @Override
        public void info(String event, String details) {
            entries.add("INFO " + event + " " + details);
        }

        @Override
        public void warn(String event, String details) {
            entries.add("WARN " + event + " " + details);
        }
    }

    static final class FakeMetrics implements MetricsRecorder {
        final Map<String, Integer> counters = new HashMap<>();

        @Override
        public void increment(String metricName) {
            counters.merge(metricName, 1, Integer::sum);
        }

        @Override
        public void recordDuration(String metricName, Duration duration) {
            // Sufficient for baseline tests; candidate may improve this fake.
        }

        int count(String name) {
            return counters.getOrDefault(name, 0);
        }
    }
}
