package com.RoleBasedTasKManager.Project6.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService
{
    private Key getSignInKey() {

        return Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes(StandardCharsets.UTF_8)
        );
    }
    private static final String SECRET_KEY =
            "mySuperSecretKeyForJwtAuthenticationProject12345678901234567890";

    public String generateToken(String email){
        System.out.println(SECRET_KEY);
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(
                        new Date(System.currentTimeMillis())
                )
                .setExpiration(
                        new Date(System.currentTimeMillis()
                                + 1000 * 60 * 60)
                )
                .signWith(
                        getSignInKey(),
                        SignatureAlgorithm.HS256
                )
                .compact();
    }

    public String extractUsername(String token) {

        Claims claims = extractAllClaims(token);

        return claims.getSubject();
    }
    private Claims extractAllClaims(String token) {
        System.out.println(SECRET_KEY);
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public boolean isTokenValid(
            String token,
            UserDetails userDetails
    ) {

        final String username =
                extractUsername(token);

        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }
    private boolean isTokenExpired(String token) {

        return extractExpiration(token)
                .before(new java.util.Date());
    }
    private Date extractExpiration(String token) {

        Claims claims = extractAllClaims(token);

        return claims.getExpiration();
    }
    public String generateAccessToken(String email){
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(
                        System.currentTimeMillis() +
                                1000*60*15
                )).signWith(getSignInKey(),SignatureAlgorithm.HS256)
                .compact();
    }
    public String generateRefreshToken(String email){
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(
                        new Date(
                                System.currentTimeMillis()+
                                        1000L*60*60*24*7
                        )
                )
                .signWith(getSignInKey(),SignatureAlgorithm.HS256)
                .compact();
    }
}