package com.gr8idea.facebook_clone.user;

public record AppUserPasswordUpdateRequest(
        String password,
        String newPassword
) {
}
