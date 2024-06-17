package com.cch.codechallengehub.service;

import com.cch.codechallengehub.domain.User;
import com.cch.codechallengehub.repository.UserRepository;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User findUser = userRepository.findByEmail(username);

		String email = findUser.getEmail();
		String password = findUser.getPassword();
		Set<String> roles = new HashSet<>();
		roles.add("USER");

		return new org.springframework.security.core.userdetails.User(email, password, getAuthorities(roles));
	}

	private Collection<? extends GrantedAuthority> getAuthorities(Set<String> roles) {
		return roles.stream()
			.map(SimpleGrantedAuthority::new)
			.collect(Collectors.toList());
	}

}
