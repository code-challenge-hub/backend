package com.cch.codechallengehub.service;

import com.cch.codechallengehub.domain.Profile;
import com.cch.codechallengehub.domain.User;
import com.cch.codechallengehub.dto.PasswordDto;
import com.cch.codechallengehub.dto.ProfileDto;
import com.cch.codechallengehub.repository.ProfileRepository;
import com.cch.codechallengehub.repository.UserRepository;
import com.cch.codechallengehub.web.exception.CustomValidationException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfileDto getProfile(String username) {
        Optional<Profile> profile = profileRepository.findByUserEmail(username);

        return profile.isPresent()? ProfileDto.toDto(profile.get()) : new ProfileDto();
    }

    public void setProfile(String username, ProfileDto profileDto) {
        // 이메일로 사용자 엔티티를 조회
        Optional<User> user = userRepository.findByEmail(username);

        if (user.isEmpty()) {
            throw new CustomValidationException("User not found with email: " + username,HttpStatus.NOT_FOUND);
        }

        Optional<Profile> existingProfile = profileRepository.findByUserEmail(username);

        Profile profile;
        if (existingProfile.isPresent()) {
            profile = existingProfile.get();
            profile.updateProfile(profileDto.getIntroduction(), profileDto.getJob(), profileDto.getCareer());
        } else {
            profile = Profile.builder()
                    .user(user.get())
                    .introduction(profileDto.getIntroduction())
                    .job(profileDto.getJob())
                    .career(profileDto.getCareer())
                    .build();
        }

        profileRepository.save(profile);
    }

    @Transactional
    public void changePassword(String username, PasswordDto passwordDto) {
        Optional<User> user = userRepository.findByEmail(username);

        if (user.isEmpty()) {
            throw new CustomValidationException("User not found with email: " + username,HttpStatus.NOT_FOUND);
        }

        if(passwordEncoder.matches(passwordDto.getPassword(),user.get().getPassword())){
            throw new CustomValidationException("This is the same password as before.");
        }

        User updateUser = user.get();
        updateUser.changePassword(passwordEncoder.encode(passwordDto.getPassword()));

    }
}
