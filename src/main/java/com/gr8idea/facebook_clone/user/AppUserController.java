package com.gr8idea.facebook_clone.user;

import com.gr8idea.facebook_clone.user.internal.AppUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class AppUserController {

    private final AppUserService appUserService;

    @GetMapping
    public AppUserDTO getUserDetails(@RequestParam("userId") String userId) {
        return appUserService.getUserDTO(userId);
    }

    @PostMapping
    public String registerUser(@RequestBody AppUserRegistrationRequest request, BindingResult bindingResult) {
        appUserService.registerUser(request, bindingResult);
        return "User registered successfully";
    }

    @PutMapping("/{userId}")
    public String updateUser(@PathVariable String userId ,@RequestBody AppUserUpdateRequest request) {
        appUserService.updateUser(request, userId);
        return "User updated successfully";
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable String userId) {
        appUserService.deleteUser(userId);
        return "User deleted successfully";
    }

}
