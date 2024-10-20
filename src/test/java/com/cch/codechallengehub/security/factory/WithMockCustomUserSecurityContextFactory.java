package com.cch.codechallengehub.security.factory;

import com.cch.codechallengehub.domain.User;
import com.cch.codechallengehub.dto.CustomUserDetails;
import com.cch.codechallengehub.security.annotation.WithMockCustomUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory
        implements WithSecurityContextFactory<WithMockCustomUser> {


    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        User user = User.builder()
                .email(customUser.email())
                .password(customUser.password())
                .role(customUser.role())
                .nickname(customUser.nickname())
                .build();

        CustomUserDetails principal =
                new CustomUserDetails(user);

        Authentication auth =
                UsernamePasswordAuthenticationToken.authenticated(principal,"",principal.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}
