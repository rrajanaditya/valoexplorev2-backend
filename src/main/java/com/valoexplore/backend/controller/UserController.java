package com.valoexplore.backend.controller;

import com.valoexplore.backend.model.User;
import com.valoexplore.backend.payload.UserUpdateRequest;
import com.valoexplore.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {


    @Autowired
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<User> getProfile(@AuthenticationPrincipal Jwt jwt) {
        User user = userService.syncUser(jwt);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/me")
    public ResponseEntity<User> updateProfile(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody UserUpdateRequest updateRequest) {
        User updatedUser = userService.updateDisplayName(jwt.getSubject(), updateRequest.getDisplayName());
        return ResponseEntity.ok(updatedUser);
    }
}