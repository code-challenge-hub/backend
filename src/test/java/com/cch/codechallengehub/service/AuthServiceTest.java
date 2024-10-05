package com.cch.codechallengehub.service;

import com.cch.codechallengehub.config.AuditingConfig;
import com.cch.codechallengehub.domain.User;
import com.cch.codechallengehub.dto.UserDto;
import com.cch.codechallengehub.repository.UserRepository;
import com.cch.codechallengehub.token.util.JWTUtil;
import com.cch.codechallengehub.token.util.RefreshTokenUtil;
import com.cch.codechallengehub.web.exception.CustomValidationException;
import com.cch.codechallengehub.web.exception.TokenException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@DataJpaTest
@Import({AuditingConfig.class})
@Transactional
class AuthServiceTest {
    @Autowired UserRepository userRepository;

    private PasswordEncoder passwordEncoder;
    @Mock private JWTUtil jwtUtil;
    @Mock private RefreshTokenUtil refreshTokenUtil;
    AuthService authService;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        authService = new AuthService(userRepository,passwordEncoder,jwtUtil,refreshTokenUtil);
    }

    @Test
    @DisplayName("새로운 user 정보로 joinProcess 성공")
    void joinProcess_success() {
        //given
        UserDto userDto = UserDto.builder()
                .nickname("test")
                .email("test@test.com")
                .password("1234")
                .build();

        // when
        authService.joinProcess(userDto);

        // then
        User savedUser = userRepository.findByEmail(userDto.getEmail()).orElse(null);
        assertNotNull(savedUser);
        assertEquals(userDto.getNickname(), savedUser.getNickname());
        assertTrue(passwordEncoder.matches(userDto.getPassword(), savedUser.getPassword()));
    }

    @Test
    @DisplayName("이미 존재하는 email로 joinProcess 실패 - CustomValidationException 발생")
    void joinProcess_emailExist_fail_throwCustomValidationException() {
        // given
        User existingUser = User.builder()
                .nickname("test")
                .email("test@test.com")
                .password(passwordEncoder.encode("1234"))
                .build();
        userRepository.save(existingUser);

        UserDto userDto = UserDto.builder()
                .nickname("test")
                .email("test@test.com")
                .password("1234")
                .build();

        // when & then
        CustomValidationException exception = assertThrows(CustomValidationException.class, () -> authService.joinProcess(userDto));
        assertEquals("This email already exists.", exception.getMessage());
    }

    @Test
    @DisplayName("쿠키에 refreshToken이 없어서 reissue 실패 - TokenException 발생")
    void reissue_refreshTokenIsNull_fail_throwTokenException() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getCookies()).thenReturn(null);

        // when & then
        TokenException exception = assertThrows(TokenException.class, () -> authService.reisuue(request, response));
        assertEquals("refresh token null", exception.getMessage());
    }
}