package com.example.tokenvalidation;

public interface DownstreamAuthorizationClient {
    AuthorizationDecision validate(String token) throws Exception;
}
