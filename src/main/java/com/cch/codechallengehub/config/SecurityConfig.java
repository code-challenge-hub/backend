package com.cch.codechallengehub.config;

import com.cch.codechallengehub.filter.CustomLogoutFilter;
import com.cch.codechallengehub.filter.JWTFilter;
import com.cch.codechallengehub.filter.LoginFilter;
import com.cch.codechallengehub.token.util.JWTUtil;
import com.cch.codechallengehub.token.util.RefreshTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${apiPrefix}")
    private String apiPrefix;

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final RefreshTokenUtil refreshTokenUtil;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        String permitUrl = apiPrefix+"/v1/auth";
        http
                .cors(cors -> cors.disable())
                .csrf((auth) -> auth.disable())
                .formLogin((auth) -> auth.disable())
                .httpBasic((auth) -> auth.disable())
                .sessionManagement((session) -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests((auth) -> auth
                                                .requestMatchers("/swagger-resources/**","/swagger-ui/**", "/swagger/**").permitAll()
                                                .requestMatchers(permitUrl+"/**").permitAll()
                                                .requestMatchers("/error","/favicon.ico").permitAll()
                                                .anyRequest().authenticated())
                .addFilterBefore(new JWTFilter(jwtUtil,permitUrl), LoginFilter.class)
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, refreshTokenUtil, apiPrefix+"/v1/auth/login"), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshTokenUtil, apiPrefix+"/v1/auth/logout"), LogoutFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
