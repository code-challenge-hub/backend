package com.cch.codechallengehub.service;

import com.cch.codechallengehub.config.AuditingConfig;
import com.cch.codechallengehub.domain.Profile;
import com.cch.codechallengehub.domain.User;
import com.cch.codechallengehub.dto.PasswordDto;
import com.cch.codechallengehub.dto.ProfileDto;
import com.cch.codechallengehub.repository.ProfileRepository;
import com.cch.codechallengehub.repository.UserRepository;
import com.cch.codechallengehub.web.exception.CustomValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@ActiveProfiles("test")
@Import({AuditingConfig.class})
@Transactional
class UserServiceTest {

    @Autowired UserRepository userRepository;
    @Autowired ProfileRepository profileRepository;
    private PasswordEncoder passwordEncoder;
    UserService userService;

    private final String testUserEmail = "test@test.com";

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserService(userRepository,profileRepository,passwordEncoder);

        // Set up test data
        User user = User.builder()
                .email(testUserEmail)
                .password(passwordEncoder.encode("qwe123!"))
                .build();
        userRepository.save(user);
    }

    @Test
    @DisplayName("존재하는 user 정보로 getProfile 성공")
    void getProfile_success() {
        // given
        User user = userRepository.findByEmail(testUserEmail).get();
        Profile profile = Profile.builder()
                .user(user)
                .introduction("Hello, I am a developer")
                .job("BE")
                .career(1)
                .build();
        profileRepository.save(profile);

        // when
        ProfileDto profileDto = userService.getProfile(testUserEmail);

        // then
        assertThat(profileDto.getIntroduction()).isEqualTo("Hello, I am a developer");
        assertThat(profileDto.getJob()).isEqualTo("BE");
        assertThat(profileDto.getCareer()).isEqualTo(1);
    }

    @Test
    @DisplayName("존재하는 user 정보로 setProfile 성공")
    void setProfile_existingUser_success() {
        // given
        ProfileDto profileDto = ProfileDto.builder()
                .introduction("Updated introduction")
                .job("Senior Developer")
                .career(10)
                .build();

        // when
        userService.setProfile(testUserEmail, profileDto);

        // then
        Optional<Profile> updatedProfile = profileRepository.findByUserEmail(testUserEmail);

        assertThat(updatedProfile).isPresent();
        assertThat(updatedProfile.get().getIntroduction()).isEqualTo("Updated introduction");
        assertThat(updatedProfile.get().getJob()).isEqualTo("Senior Developer");
        assertThat(updatedProfile.get().getCareer()).isEqualTo(10);
    }


    @Test
    @DisplayName("존재하지 않는 user 정보로 setProfile 실패 - CustomValidationException 발생")
    void setProfile_fail_throwsCustomValidationException() {
        // given
        ProfileDto profileDto = ProfileDto.builder()
                .introduction("Updated introduction")
                .job("Senior Developer")
                .career(10)
                .build();
        String nonexistentuser = "nonexistentuser@example.com";

        // when & then
        CustomValidationException exception =
                assertThrows(CustomValidationException.class, () ->
                userService.setProfile(nonexistentuser, profileDto));

        assertThat(exception.getMessage()).isEqualTo("User not found with email: " + nonexistentuser);
    }

    @Test
    @DisplayName("존재하는 user 정보로 changePassword 성공")
    void changePassword_existingUser_success() {
        // given
        PasswordDto passwordDto = PasswordDto.builder()
                                .password("1234")
                                .build();

        // when
        userService.changePassword(testUserEmail, passwordDto);

        // then
        User user = userRepository.findByEmail(testUserEmail).get();
        assertThat(passwordEncoder.matches("1234", user.getPassword())).isTrue();
    }

    @Test
    @DisplayName("같은 password로 changePassword 실패 - CustomValidationException 발생")
    void changePassword_samePassword_fail_throwsCustomValidationException() {
        // given
        PasswordDto passwordDto = PasswordDto.builder()
                .password("qwe123!")
                .build();

        // when & then
        CustomValidationException exception = assertThrows(CustomValidationException.class, () ->
                userService.changePassword(testUserEmail, passwordDto));
        assertThat(exception.getMessage()).isEqualTo("This is the same password as before.");
    }

    @Test
    @DisplayName("존재하지 않는 user 정보로 changePassword 실패 - CustomValidationException 발생")
    void changePassword_nonExistentUser_fail_throwsCustomValidationException() {
        // given
        PasswordDto passwordDto = PasswordDto.builder()
                .password("1234")
                .build();
        String nonexistentuser = "nonexistentuser@example.com";

        // when & then
        CustomValidationException exception = assertThrows(CustomValidationException.class, () ->
                userService.changePassword(nonexistentuser, passwordDto));
        assertThat(exception.getMessage()).isEqualTo("User not found with email: "+nonexistentuser);
    }


}