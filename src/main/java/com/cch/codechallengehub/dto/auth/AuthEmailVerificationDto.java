package com.cch.codechallengehub.dto.auth;

import lombok.*;

import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthEmailVerificationDto {
    private String email;
    private String verificationCode;  // 인증코드
    private int attemptCount;   //시도횟수
    private boolean isDone; //완료여부
}
