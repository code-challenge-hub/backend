package com.cch.codechallengehub.repository;

import com.cch.codechallengehub.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByUserEmail(String email);
}
