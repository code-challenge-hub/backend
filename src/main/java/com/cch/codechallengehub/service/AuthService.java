package com.cch.codechallengehub.service;

import com.cch.codechallengehub.constants.UserRole;
import com.cch.codechallengehub.domain.User;
import com.cch.codechallengehub.dto.JoinDTO;
import com.cch.codechallengehub.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {

        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void joinProcess(JoinDTO joinDTO) {

        String nickname = joinDTO.getNickname();
        String email = joinDTO.getEmail();
        String password = joinDTO.getPassword();

        Boolean isExist = userRepository.existsByEmail(email);

        if (isExist) {
            return;
        }

        User data = new User(nickname, email, bCryptPasswordEncoder.encode(password), UserRole.USER.toString());

        userRepository.save(data);
    }
}
