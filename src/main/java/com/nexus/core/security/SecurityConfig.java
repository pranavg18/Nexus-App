package com.nexus.core.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF (Cross-Site Request Forgery) because we're using JWT tokens, not cookies
                .csrf(csrf->csrf.disable())

                // Set the rules for our endpoints
                .authorizeHttpRequests(auth->auth
                        // Allow anyone to access register, login, and the Swagger UI (no token needed)
                        .requestMatchers("/api/auth/register", "/api/auth/login", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        // Every other request requires a valid token
                        .anyRequest().authenticated()
                )

                // Tell Spring Security not to save sessions in memory anymore (stateless now)
                .sessionManagement(sess->sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Tell Spring to run JwtAuthFilter before its default filters
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
