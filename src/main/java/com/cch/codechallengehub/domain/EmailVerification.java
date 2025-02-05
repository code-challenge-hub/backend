package com.cch.codechallengehub.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.io.Serializable;

@Getter
@RedisHash(value = "emailVerification")
public class EmailVerification implements Serializable {

    @Id
    private String email;
    private String verificationCode;
    private int attemptCount;
    private boolean isDone;

    @TimeToLive
    private Long ttl;

    @Builder
    public EmailVerification(String email, String verificationCode, int attemptCount, boolean isDone, Long ttl) {
        this.email = email;
        this.verificationCode = verificationCode;
        this.attemptCount = attemptCount;
        this.isDone = isDone;
        this.ttl = ttl;
    }

    public void plusAttemptCount() {
        this.attemptCount++;
    }

    public void updateStatus(boolean isDone) {
        this.isDone = isDone;
    }
}
