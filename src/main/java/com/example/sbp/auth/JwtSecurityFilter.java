package com.example.sbp.auth;

import com.example.sbp.common.component.JwtTokenManager;
import com.example.sbp.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class JwtSecurityFilter extends OncePerRequestFilter {
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenManager jwtTokenManager;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        extractToken(request)
            .filter(jwtTokenManager::isValid)
            .map(jwtTokenManager::parse)
            .map(claims -> claims.get("uid", Long.class))
            .flatMap(userRepository::findById)
            .map(AuthenticatedUser::from)
            .map(userDetails -> UsernamePasswordAuthenticationToken.authenticated(userDetails, null, userDetails.getAuthorities()))
            .ifPresent(authentication -> SecurityContextHolder.getContext().setAuthentication(authentication));

        filterChain.doFilter(request, response);
    }

    private Optional<String> extractToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(AUTHORIZATION))
            .filter(bearerToken -> bearerToken.startsWith(BEARER_PREFIX))
            .map(bearerToken -> bearerToken.substring(BEARER_PREFIX.length()));
    }
}