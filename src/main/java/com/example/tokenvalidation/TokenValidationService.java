package com.example.tokenvalidation;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

/**
 * Starter implementation. It is deliberately not production-ready.
 */
public final class TokenValidationService {
    private static final int MAX_ATTEMPTS = 3;

    private final DownstreamAuthorizationClient downstream;
    private final AuditLogger auditLogger;
    private final MetricsRecorder metrics;
    private final Clock clock;

    public TokenValidationService(
            DownstreamAuthorizationClient downstream,
            AuditLogger auditLogger,
            MetricsRecorder metrics,
            Clock clock) {
        this.downstream = Objects.requireNonNull(downstream);
        this.auditLogger = Objects.requireNonNull(auditLogger);
        this.metrics = Objects.requireNonNull(metrics);
        this.clock = Objects.requireNonNull(clock);
    }

    public ValidationResult validate(String token, String customerId) {
        if (token == null || token.isBlank()) {
            return ValidationResult.invalid("missing token");
        }

        Instant start = clock.instant();
        auditLogger.info("validation_started", "token=" + token + ", customerId=" + customerId);

        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            try {
                AuthorizationDecision decision = downstream.validate(token);
                metrics.increment("validation.success");
                metrics.recordDuration("validation.duration", Duration.between(start, clock.instant()));

                if (decision.active()) {
                    auditLogger.info("validation_succeeded", "customerId=" + customerId);
                    return ValidationResult.valid(decision.subject());
                }

                auditLogger.warn("validation_rejected", "token=" + token + ", reason=" + decision.reason());
                return ValidationResult.invalid(decision.reason());
            } catch (Exception failure) {
                metrics.increment("validation.retry");
                auditLogger.warn(
                        "downstream_failure",
                        "attempt=" + attempt + ", token=" + token + ", error=" + failure.getMessage());
            }
        }

        metrics.increment("validation.failure");
        return ValidationResult.unavailable();
    }
}
