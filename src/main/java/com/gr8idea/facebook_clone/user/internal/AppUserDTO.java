package com.gr8idea.facebook_clone.user.internal;

import java.util.Date;
import java.util.List;

public record AppUserDTO(
        String email,
        String firstName,
        String lastName,
        Gender gender,
        Date dateOfBirth,
        List<AppUser> friendsList
) {}
