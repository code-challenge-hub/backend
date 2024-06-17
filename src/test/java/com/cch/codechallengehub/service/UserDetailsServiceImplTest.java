package com.cch.codechallengehub.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.cch.codechallengehub.domain.User;
import com.cch.codechallengehub.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
class UserDetailsServiceImplTest {

	@Autowired
	UserRepository userRepository;

	UserDetailsService userDetailsService;

	@BeforeEach
	void setUp() {
		userDetailsService = new UserDetailsServiceImpl(userRepository);
	}

	@Test
	void loadUserByUsernameTest() {
	    // given
		User user = User.builder()
			.email("gasfa@aafas.com")
			.password("1234")
			.build();
		userRepository.save(user);
		String email = user.getEmail();
		String password = user.getPassword();
		// when
		UserDetails userDetails = userDetailsService.loadUserByUsername(email);
		// then
		assertThat(email).isEqualTo(userDetails.getUsername());
		assertThat(password).isEqualTo(userDetails.getPassword());

	}

}