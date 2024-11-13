package com.cch.codechallengehub.repository;

import com.cch.codechallengehub.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
