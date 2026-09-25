package com.example.tokenvalidation;

public record AuthorizationDecision(boolean active, String subject, String reason) {
    public static AuthorizationDecision active(String subject) {
        return new AuthorizationDecision(true, subject, "active");
    }

    public static AuthorizationDecision inactive(String reason) {
        return new AuthorizationDecision(false, null, reason);
    }
}
