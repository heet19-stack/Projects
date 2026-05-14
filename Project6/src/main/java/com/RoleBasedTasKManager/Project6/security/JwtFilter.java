package com.RoleBasedTasKManager.Project6.security;
import com.RoleBasedTasKManager.Project6.security.CustomUserDetailsService;
import com.RoleBasedTasKManager.Project6.security.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getServletPath();

        // Public APIs
        if (
                path.equals("/auth/signup") ||
                        path.equals("/auth/login")
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        // Get Authorization header
        final String authHeader =
                request.getHeader("Authorization");

        String jwt = null;
        String email = null;

        // Check Bearer token
        if(authHeader != null &&
                authHeader.startsWith("Bearer ")) {

            jwt = authHeader.replace("Bearer","".trim());

            System.out.println("JWT -> " + jwt + " <-");

            email = jwtService.extractUsername(jwt);
        }

        // Authenticate user
        if (email != null &&
                SecurityContextHolder.getContext()
                        .getAuthentication() == null) {

            UserDetails userDetails =
                    customUserDetailsService
                            .loadUserByUsername(email);

            // Validate token
            if (jwtService.isTokenValid(jwt, userDetails)) {

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authToken);
            }
        }

        // Continue request
        filterChain.doFilter(request, response);
    }
}