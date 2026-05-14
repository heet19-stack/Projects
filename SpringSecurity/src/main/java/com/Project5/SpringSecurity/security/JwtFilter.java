package com.Project5.SpringSecurity.security;

import com.Project5.SpringSecurity.service.CustomUserDetailsService;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter
{
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain)
//            throws ServletException, IOException
//    {
//        String authHeader = request.getHeader("Authorization");
//
//        String token = null;
//        String username = null;
//
//        //Check header
//        if (authHeader != null && authHeader.startsWith("Bearer")){
//            token = authHeader.substring(7);
//
//            username = jwtUtil.extractUsername(token);
//        }
//
//        //validate token
//        if (username != null && jwtUtil.validateToken(token,username)){
//            System.out.println("Valid token");
//        }
//        filterChain.doFilter(request,response);
//    }
@Override
protected void doFilterInternal(HttpServletRequest request,
                                HttpServletResponse response,
                                FilterChain filterChain)
        throws ServletException, IOException {

    String authHeader = request.getHeader("Authorization");

    String token = null;
    String username = null;

    // Check Authorization header
    if (authHeader != null && authHeader.startsWith("Bearer ")) {

        token = authHeader.substring(7);

        username = jwtUtil.extractUsername(token);
    }

    // Validate token
    if (username != null
            && SecurityContextHolder.getContext().getAuthentication() == null
            && jwtUtil.validateToken(token, username)) {

        UserDetails userDetails =
                customUserDetailsService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        SecurityContextHolder.getContext()
                .setAuthentication(authToken);
    }

    filterChain.doFilter(request, response);
}
}
