package com.cch.codechallengehub.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Getter
@RedisHash(value = "JoinEmail", timeToLive = 900)
public class JoinEmail implements Serializable {
    @Id
    private String email;

    @Builder
    public JoinEmail(String email) {
        this.email = email;
    }
}
