package com.example.sbp.auth;

import com.example.sbp.common.support.utils.JsonUtils;
import com.example.sbp.common.web.ApiResponse;
import com.example.sbp.user.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.util.List;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtSecurityFilter jwtSecurityFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors
                .configurationSource(corsConfigurationSource())
            )
            .csrf(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorize())
            .formLogin(AbstractHttpConfigurer::disable)
            .sessionManagement(configurer ->
                configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .exceptionHandling(configurer -> configurer
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .accessDeniedHandler(new CustomAccessDeniedHandler())
            );

        http.addFilterBefore(jwtSecurityFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        var corsConfiguration = new CorsConfiguration();

        corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.setAllowedHeaders(
            List.of(
                "Access-Control-Allow-Headers",
                "Origin",
                "Accept",
                "X-Requested-With",
                "Content-Type",
                "Access-Control-Request-Method",
                "Access-control-allow-credentials",
                "Authorization"
            )
        );
        corsConfiguration.setAllowedMethods(
            List.of(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.DELETE.name(),
                HttpMethod.PATCH.name(),
                HttpMethod.OPTIONS.name()
            )
        );
        corsConfiguration.setAllowCredentials(true);

        var corsConfigurationSource = new UrlBasedCorsConfigurationSource();

        corsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        return corsConfigurationSource;
    }

    private Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> authorize() {
        return auth -> auth
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/users/sign-up").permitAll()
            .requestMatchers(HttpMethod.POST, "/users/login").permitAll()
            .requestMatchers(HttpMethod.GET, "/posts").permitAll()
            .requestMatchers(HttpMethod.GET, "/posts/{id}").permitAll()
            .requestMatchers(HttpMethod.GET, "/api-docs/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/actuator/**").permitAll()
            .requestMatchers("admin/**").hasRole(User.RoleType.ADMIN.name())
            .anyRequest().authenticated();
    }

    private static class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(JsonUtils.stringify(ApiResponse.unauthorized()));
        }
    }

    private static class CustomAccessDeniedHandler implements AccessDeniedHandler {
        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(JsonUtils.stringify(ApiResponse.forbidden()));
        }
    }
}
