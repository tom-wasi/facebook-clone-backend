package com.gr8idea.facebook_clone.user;

import com.gr8idea.facebook_clone.user.internal.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface AppUserRepository extends JpaRepository<AppUser, String> {

    Optional<AppUser> findUserByEmail(String email);
}
