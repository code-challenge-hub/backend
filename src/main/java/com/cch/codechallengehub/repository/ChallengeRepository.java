package com.cch.codechallengehub.repository;

import com.cch.codechallengehub.domain.Challenge;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

	Optional<Challenge> findByChallengeName(String challengeName);

}
