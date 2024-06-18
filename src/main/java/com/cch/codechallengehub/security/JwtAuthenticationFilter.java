package com.cch.codechallengehub.security;

import static com.cch.codechallengehub.security.AuthorizationHeader.AUTHORIZATION;
import static com.cch.codechallengehub.security.AuthorizationHeader.BEARER;
import static org.springframework.util.StringUtils.hasText;

import com.cch.codechallengehub.service.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;
	private final UserDetailsService userDetailsService;
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		String token = getTokenByRequest(request);

		if (!hasText(token)) {
			filterChain.doFilter(request, response);
			return;
		}

		try {
			if (jwtTokenProvider.verifyToken(token)) {
				String email = jwtTokenProvider.getEmailFromToken(token);
				UserDetails userDetails = userDetailsService.loadUserByUsername(email);

				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
					userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(
					new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (ExpiredJwtException ex) {
			// .. expired Token
		}
		filterChain.doFilter(request, response);

	}

	private String getTokenByRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHORIZATION.getValue());
		if (hasText(bearerToken) && bearerToken.startsWith(BEARER.getValue())) {
			return bearerToken.substring(7);
		}
		return null;
	}
}
