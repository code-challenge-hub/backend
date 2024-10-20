package com.cch.codechallengehub.repository;

import com.cch.codechallengehub.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}