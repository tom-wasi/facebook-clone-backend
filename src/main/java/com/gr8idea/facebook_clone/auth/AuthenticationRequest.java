package com.gr8idea.facebook_clone.auth;

public record AuthenticationRequest(
        String email,
        String password
) {}
