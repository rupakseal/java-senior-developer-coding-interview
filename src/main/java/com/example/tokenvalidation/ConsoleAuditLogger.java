package com.example.tokenvalidation;

public final class ConsoleAuditLogger implements AuditLogger {
    @Override
    public void info(String event, String details) {
        System.out.println("INFO event=" + event + " " + details);
    }

    @Override
    public void warn(String event, String details) {
        System.err.println("WARN event=" + event + " " + details);
    }
}
