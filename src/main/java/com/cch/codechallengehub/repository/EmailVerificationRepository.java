package com.cch.codechallengehub.repository;

import com.cch.codechallengehub.domain.EmailVerification;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EmailVerificationRepository extends CrudRepository<EmailVerification, String> {
    void deleteById(String id);
    Optional<EmailVerification> findById(String id);
}