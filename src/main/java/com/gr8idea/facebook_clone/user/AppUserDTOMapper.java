package com.gr8idea.facebook_clone.user;

import com.gr8idea.facebook_clone.user.internal.AppUser;
import com.gr8idea.facebook_clone.user.internal.AppUserDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class AppUserDTOMapper implements Function<AppUser, AppUserDTO> {
    @Override
    public AppUserDTO apply(AppUser appUser) {
        return new AppUserDTO(
                appUser.getEmail(),
                appUser.getFirstName(),
                appUser.getLastName(),
                appUser.getGender(),
                appUser.getDateOfBirth(),
                appUser.getFriendsList()
        );
    }
}