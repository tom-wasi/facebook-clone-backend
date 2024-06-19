package com.gr8idea.facebook_clone.user;

import com.gr8idea.facebook_clone.user.internal.Gender;

import java.util.Date;

public record AppUserRegistrationRequest(
        String email,
        String password,
        String firstName,
        String lastName,
        Gender gender,
        Date dateOfBirth
) {}
