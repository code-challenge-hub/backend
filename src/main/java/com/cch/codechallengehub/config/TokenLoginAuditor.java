package com.cch.codechallengehub.config;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class TokenLoginAuditor implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("");
    }
}
