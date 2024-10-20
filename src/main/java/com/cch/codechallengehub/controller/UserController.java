package com.cch.codechallengehub.controller;

import com.cch.codechallengehub.dto.PasswordDto;
import com.cch.codechallengehub.dto.ProfileDto;
import com.cch.codechallengehub.service.UserService;
import com.cch.codechallengehub.web.exception.AuthException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "${apiPrefix}/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<ProfileDto> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        ProfileDto result = userService.getProfile(userDetails.getUsername());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/profile")
    public ResponseEntity<Void> setProfile(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody ProfileDto profileDto) {
        userService.setProfile(userDetails.getUsername(), profileDto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/password")
    public ResponseEntity<Void> changePassword(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody PasswordDto passwordDto) {
        userService.changePassword(userDetails.getUsername(), passwordDto);
        return ResponseEntity.ok().build();
    }
}
