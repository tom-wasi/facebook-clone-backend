package com.gr8idea.facebook_clone.user;

import com.gr8idea.facebook_clone.exception.UserNotFoundException;
import com.gr8idea.facebook_clone.mail.confirmation.ConfirmationToken;
import com.gr8idea.facebook_clone.user.internal.AppUser;
import com.gr8idea.facebook_clone.user.internal.AppUserDTO;
import com.gr8idea.facebook_clone.user.internal.UserPrincipal;
import com.gr8idea.facebook_clone.mail.MailService;
import com.gr8idea.facebook_clone.mail.confirmation.ConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class AppUserService implements UserDetailsService {

    private final AppUserDao appUserDao;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final MailService mailService;
    private final AppUserDTOMapper appUserDTOMapper;

    public AppUserService(
            @Qualifier("app_user_jpa") AppUserDao appUserDao,
            PasswordEncoder passwordEncoder,
            ConfirmationTokenRepository confirmationTokenRepository,
            MailService mailService, AppUserDTOMapper appUserDTOMapper) {
        this.appUserDao = appUserDao;
        this.passwordEncoder = passwordEncoder;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.mailService = mailService;
        this.appUserDTOMapper = appUserDTOMapper;
    }

    public void registerUser(AppUserRegistrationRequest request, BindingResult bindingResult) {
        validateRegistrationRequest(request, bindingResult);
        AppUser user = buildUser(request);
        appUserDao.insertAppUser(user);
        sendConfirmationEmail(user);
    }

    public AppUserDTO getUserDTO(String userId) {
        AppUser appUser = appUserDao.selectAppUserByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return appUserDTOMapper.apply(appUser);
    }

    public AppUser getUser(String userId) {
        return appUserDao.selectAppUserByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public void updateUser(AppUserUpdateRequest updateRequest, String userId) {
        AppUser appUser = appUserDao.selectAppUserByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("user with email: [%s] not found".formatted(userId)));

        if (validateUpdateRequest(updateRequest, appUser)) {
            appUserDao.updateAppUser(appUser);
        } else {
            throw new IllegalArgumentException("No changes to update");
        }
    }

    public void deleteUser(String userId) {
        AppUser appUser = appUserDao.selectAppUserByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        appUserDao.deleteAppUserByUserId(userId);
    }

    boolean validateUpdateRequest(AppUserUpdateRequest updateRequest, AppUser appUser) {

        boolean changesFlag = false;

        if (updateRequest.firstName() != null && !updateRequest.firstName().equals(appUser.getFirstName())) {
            appUser.setFirstName(updateRequest.firstName());
            changesFlag = true;
        }

        if (updateRequest.lastName() != null && !updateRequest.lastName().equals(appUser.getLastName())) {
            appUser.setLastName(updateRequest.lastName());
            changesFlag = true;
        }

        if (updateRequest.gender() != null && !updateRequest.gender().equals(appUser.getGender())) {
            appUser.setGender(updateRequest.gender());
            changesFlag = true;
        }

        if (updateRequest.dateOfBirth() != null && !updateRequest.dateOfBirth().equals(appUser.getDateOfBirth())) {
            appUser.setDateOfBirth(updateRequest.dateOfBirth());
            changesFlag = true;
        }
        return changesFlag;
    }


    void sendConfirmationEmail(AppUser user) {
        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        confirmationTokenRepository.save(confirmationToken);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete the registration!");
        mailMessage.setText("To confirm your account, please click here: "
                + "http://localhost:8080/confirm-account/" + confirmationToken.getConfirmationToken());
        mailService.sendEmail(mailMessage);
    }

    public ResponseEntity<?> confirmEmail(String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        if (token != null) {
            AppUser user = appUserDao.selectAppUserByEmail(token.getAppUser().getEmail()).orElseThrow();
            user.setIsEnabled(true);
            appUserDao.insertAppUser(user);
            return ResponseEntity.ok("Email verified successfully!");
        }
        return ResponseEntity.badRequest().body("Error: Couldn't verify email");

    }

    private AppUser buildUser(AppUserRegistrationRequest request) {
        String userId;
        do {
            userId = UUID.randomUUID().toString();
        } while (appUserDao.existsAppUserByUserId(userId));

        return AppUser.builder()
                .id(userId)
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .firstName(request.firstName())
                .lastName(request.lastName())
                .gender(request.gender())
                .dateOfBirth(request.dateOfBirth())
                .isEnabled(false)
                .build();
    }

    public void validateRegistrationRequest(AppUserRegistrationRequest request, BindingResult bindingResult) {

        if (emailExists(request.email())) {
            bindingResult.rejectValue("email", "email.exists", "Email already registered");
        }

        if (request.password() != null && request.password().length() < 8) {
            bindingResult.rejectValue("password", "password.short", "Password must be at least 8 characters long");
        }

        if (request.firstName().isEmpty()) {
            bindingResult.rejectValue("firstName", "fistName.short", "Must provide a first name.");
        }

        if (request.lastName().isEmpty()) {
            bindingResult.rejectValue("lastName", "lastName.short", "Must provide a last name.");
        }

        if (!isDateOfBirthValid(request.dateOfBirth())) {
            bindingResult.rejectValue("dateOfBirth", "dateOfBirth.invalid", "Date of birth must be between 01-01-1900 and today.");
        }
    }

    private boolean isDateOfBirthValid(Date dateOfBirth) {
        LocalDate dob = dateOfBirth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate now = LocalDate.now();
        LocalDate minDate = LocalDate.parse("01-01-1900", DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        return !(dob.isAfter(now) || dob.isBefore(minDate));
    }

    public boolean emailExists(String email) {
        return appUserDao.existsAppUserWithEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> user = appUserDao.selectAppUserByEmail(username);
        return user.map(UserPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
