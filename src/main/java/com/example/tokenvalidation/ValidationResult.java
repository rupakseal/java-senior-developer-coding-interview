package com.example.tokenvalidation;

public record ValidationResult(Status status, String subject, String message) {
    public enum Status {
        VALID,
        INVALID,
        TEMPORARILY_UNAVAILABLE
    }

    public static ValidationResult valid(String subject) {
        return new ValidationResult(Status.VALID, subject, "token active");
    }

    public static ValidationResult invalid(String reason) {
        return new ValidationResult(Status.INVALID, null, reason);
    }

    public static ValidationResult unavailable() {
        return new ValidationResult(Status.TEMPORARILY_UNAVAILABLE, null, "authorization dependency unavailable");
    }
}
