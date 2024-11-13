package com.cch.codechallengehub.web.controller;

import com.cch.codechallengehub.dto.user.PasswordDto;
import com.cch.codechallengehub.dto.user.ProfileDto;
import com.cch.codechallengehub.service.UserService;
import com.cch.codechallengehub.web.dto.user.PasswordModifyRequest;
import com.cch.codechallengehub.web.dto.user.ProfileCreateRequest;
import com.cch.codechallengehub.web.dto.user.ProfileResponse;
import com.cch.codechallengehub.web.mapper.user.PasswordModifyMapper;
import com.cch.codechallengehub.web.mapper.user.ProfileCreateMapper;
import com.cch.codechallengehub.web.mapper.user.ProfileResponseMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "${apiPrefix}")
public class UserController {

    private final UserService userService;
    private final ProfileResponseMapper profileResponseMapper;
    private final ProfileCreateMapper profileCreateMapper;
    private final PasswordModifyMapper passwordModifyMapper;

    @GetMapping("/v1/user/profile")
    public ResponseEntity<ProfileResponse> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        ProfileDto dto = userService.getProfile(userDetails.getUsername());
        ProfileResponse response = profileResponseMapper.profileDtoToResponse(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/v1/user/profile")
    public ResponseEntity<Void> setProfile(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody ProfileCreateRequest request) {
        ProfileDto profileDto = profileCreateMapper.requestToDto(request);
        userService.setProfile(userDetails.getUsername(), profileDto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/v1/user/password")
    public ResponseEntity<Void> changePassword(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody PasswordModifyRequest request) {
        PasswordDto passwordDto = passwordModifyMapper.requestToDto(request);
        userService.changePassword(userDetails.getUsername(), passwordDto);
        return ResponseEntity.ok().build();
    }
}
