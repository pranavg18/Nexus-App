package com.nexus.core.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Grab the "Authorization" header from the incoming request
        final String authHeader = request.getHeader("Authorization");

        // If there is no token, or it doesn't start with "Bearer ", ignore it and move on
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract the token string (skip the "Bearer " part)
        final String jwt = authHeader.substring(7);

        // Use the utility to read the username
        final String username = jwtUtil.extractUsername(jwt);

        // If we found a username and Spring Security doesn't already know who is logged in
        if (username != null && SecurityContextHolder.getContext().getAuthentication() != null) {
            // Validate the token using the secret key
            if (jwtUtil.validateToken(jwt, username)) {
                // Tell Spring Security that this user's token is legit so let them in
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // Continue sending the request to the controller
        filterChain.doFilter(request, response);
    }
}
