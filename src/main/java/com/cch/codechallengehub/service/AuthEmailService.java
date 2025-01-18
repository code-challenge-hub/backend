package com.cch.codechallengehub.service;

import com.cch.codechallengehub.domain.EmailVerification;
import com.cch.codechallengehub.domain.JoinEmail;
import com.cch.codechallengehub.dto.auth.AuthEmailVerificationDto;
import com.cch.codechallengehub.repository.EmailVerificationRepository;
import com.cch.codechallengehub.repository.JoinEmailRepository;
import com.cch.codechallengehub.repository.UserRepository;
import com.cch.codechallengehub.web.exception.custom.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthEmailService {

    private final AwsSesService sesService;
    private final UserRepository userRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final JoinEmailRepository joinEmailRepository;

    @Transactional
    public void sendVerificationCodeEmail(String email) {

        // 이메일 가입 확인
        Boolean isExist = userRepository.existsByEmail(email);

        if (isExist) {
            throw new BadRequestException("This email already exists.");
        }

        //인증번호 생성
        String code = createCode();
        String mailSubject = "회원가입 인증번호";
        String mailBody = "<h2>요청하신 인증 번호입니다.</h2><h1>"+code+"</h1>";

        sesService.sendEmail(email, mailSubject, mailBody);

        //redis에 인증번호 저장 (제한시간 15분)
        EmailVerification verification = EmailVerification.builder()
                .email(email)
                .verificationCode(code)
                .attemptCount(0)
                .isDone(false)
                .ttl(900L)
                .build();

        emailVerificationRepository.save(verification);
    }

    private String createCode() {
        Random random = new Random();
        StringBuilder key = new StringBuilder();

        for (int i = 0; i < 7; i++) {
            int index = random.nextInt(2);

            switch (index) {
                case 0 -> key.append((char) (random.nextInt(26) + 65));
                case 1 -> key.append(random.nextInt(10));
            }
        }
        return key.toString();
    }

    @Transactional
    public AuthEmailVerificationDto verificationCode(String email, String code) {
        //이메일 존재 확인 => 이메일 인증 시간이 초과되었다.
        EmailVerification verification = emailVerificationRepository.findById(email)
                .orElseThrow(() -> new BadRequestException("이메일 인증 시간이 초과되었습니다."));

        verification.plusAttemptCount();

        //이미 인증이 롼료된 상태{
        if (verification.isDone()) {
            throw new BadRequestException("이미 인증이 완료된 상태입니다.");
        }

        //너무 많은시도
        if (verification.getAttemptCount() > 5) {
            throw new BadRequestException("너무 많은 시도를 하였습니다.");
        }

        //인증번호가 올바를때만 상태 변경
        if (verification.getVerificationCode().equals(code)) {
            verification.updateStatus(true);
            //회원가입을 위해 인증완료했다는 정보를 따로 redis에 저장

            joinEmailRepository.save(JoinEmail.builder().email(verification.getEmail()).build());
        }

        emailVerificationRepository.save(verification);

        return AuthEmailVerificationDto.builder()
                .email(verification.getEmail())
                .verificationCode(code)
                .attemptCount(verification.getAttemptCount())
                .isDone(verification.isDone())
                .build();

    }
}
