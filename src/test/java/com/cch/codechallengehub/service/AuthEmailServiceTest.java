package com.cch.codechallengehub.service;

import com.cch.codechallengehub.config.AuditingConfig;
import com.cch.codechallengehub.config.AwsConfig;
import com.cch.codechallengehub.config.TestRedisConfiguration;
import com.cch.codechallengehub.domain.EmailVerification;
import com.cch.codechallengehub.dto.auth.AuthEmailVerificationDto;
import com.cch.codechallengehub.repository.EmailVerificationRepository;
import com.cch.codechallengehub.repository.JoinEmailRepository;
import com.cch.codechallengehub.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.sesv2.SesV2Client;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@DataJpaTest
@EnableRedisRepositories(basePackages = "com.cch.codechallengehub.repository")
@Import({TestRedisConfiguration.class, AuditingConfig.class, AwsConfig.class})
@Transactional
class AuthEmailServiceTest {

    @Autowired
    SesV2Client client;
    AwsSesService sesService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    EmailVerificationRepository emailVerificationRepository;
    @Autowired
    JoinEmailRepository joinEmailRepository;

    AuthEmailService authEmailService;

    @BeforeEach
    void setUp() {
        sesService = new AwsSesService(client);
        ReflectionTestUtils.setField(sesService, "sendMailTo", "no-reply@codechallenge.kro.kr");
        authEmailService = new AuthEmailService(sesService, userRepository, emailVerificationRepository, joinEmailRepository);
    }

    @Test
    @DisplayName("이메일 발송 성공")
    void sendVerificationCodeEmail_success() {
        String email = "haeunchoi.dev@gmail.com";
        authEmailService.sendVerificationCodeEmail(email);

        Optional<EmailVerification> verification = emailVerificationRepository.findById(email);

        assertNotNull(verification);
    }

    @Test
    @DisplayName("인증 코드 검증 API 성공")
    void verificationCode_success() {
        String email = "haeunchoi.dev@gmail.com";
        String code = "123456";
        EmailVerification verification = EmailVerification.builder()
                .email(email)
                .verificationCode(code)
                .attemptCount(0)
                .isDone(false)
                .ttl(900L)
                .build();
        emailVerificationRepository.save(verification);

        AuthEmailVerificationDto dto = authEmailService.verificationCode(email, code);

        assertThat(dto.getEmail()).isEqualTo(email);
        assertThat(dto.getVerificationCode()).isEqualTo(code);
        assertThat(dto.getAttemptCount()).isEqualTo(1);
        assertThat(dto.isDone()).isTrue();

    }
}