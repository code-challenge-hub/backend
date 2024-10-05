package com.cch.codechallengehub.security;

import com.cch.codechallengehub.config.SecurityConfig;
import com.cch.codechallengehub.token.util.JWTUtil;
import com.cch.codechallengehub.token.util.RefreshTokenUtil;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

@Import(SecurityConfig.class)
public class CommonSecurityTest {
    @MockBean JWTUtil jwtUtil;
    @MockBean RefreshTokenUtil refreshTokenUtil;
}
