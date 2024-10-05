package com.cch.codechallengehub.repository;

import com.cch.codechallengehub.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    boolean existsById(String id);
    void deleteById(String id);
    Optional<RefreshToken> findById(String id);
}