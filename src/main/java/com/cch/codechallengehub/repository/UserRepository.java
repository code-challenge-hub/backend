package com.cch.codechallengehub.repository;

import com.cch.codechallengehub.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

	User findByEmail(String email);

}
