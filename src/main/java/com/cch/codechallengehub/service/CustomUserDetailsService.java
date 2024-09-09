package com.cch.codechallengehub.service;

import com.cch.codechallengehub.domain.User;
import com.cch.codechallengehub.dto.CustomUserDetails;
import com.cch.codechallengehub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService  implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> userData = userRepository.findByEmail(username);

        if (userData.isPresent()) {
            return new CustomUserDetails(userData.get());
        }

        return null;
    }
}
