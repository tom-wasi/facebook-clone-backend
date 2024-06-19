package com.gr8idea.facebook_clone.user;

import com.gr8idea.facebook_clone.user.internal.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("app_user_jpa")
@RequiredArgsConstructor
public class AppUserJPADataAccessService implements AppUserDao {

    private final AppUserRepository appUserRepository;

    @Override
    public Optional<AppUser> selectAppUserByUserId(String userId) {
        return appUserRepository.findById(userId);
    }

    @Override
    public void insertAppUser(AppUser appUser) {
        appUserRepository.save(appUser);
    }

    @Override
    public boolean existsAppUserWithEmail(String email) {
        return false;
    }

    @Override
    public boolean existsAppUserByUserId(String userId) {
        return false;
    }

    @Override
    public void deleteAppUserByUserId(String userId) {

    }

    @Override
    public void updateAppUser(AppUser update) {

    }

    @Override
    public Optional<AppUser> selectAppUserByEmail(String email) {
        return appUserRepository.findUserByEmail(email);
    }
}
