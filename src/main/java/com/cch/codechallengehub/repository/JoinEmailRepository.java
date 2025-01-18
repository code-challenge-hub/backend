package com.cch.codechallengehub.repository;

import com.cch.codechallengehub.domain.JoinEmail;
import org.springframework.data.repository.CrudRepository;

public interface JoinEmailRepository extends CrudRepository<JoinEmail, String> {
    boolean existsById(String id);
}
