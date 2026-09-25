package com.example.tokenvalidation;

public interface AuditLogger {
    void info(String event, String details);

    void warn(String event, String details);
}
