package com.Project5.SpringSecurity.security;

import com.Project5.SpringSecurity.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.CachingUserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil
{
    private final String SECRET_KEY = "mySuperSecretKeyForJwtTokenGeneration123456789";




    //generates token
    public String generateToken(String username,String role){
        return Jwts.builder()
                .setSubject(username)
                .claim("role",role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*60))
                .signWith(SignatureAlgorithm.HS256,SECRET_KEY)
                .compact();
    }

    //Extracts username from the token
    public String extractUsername(String token){
        return extractAllClaims(token).getSubject();
    }

   //Decodes the token + Verifies it
    public Claims extractAllClaims(String token){
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    //Extracts role from the token
    public String extractRole(String token){
        return
                extractAllClaims(token).get("role",String.class);
    }

    //Checks whether the token is expired or not
    public boolean isTokenExpired(String token){
        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());
    }

    //Final check whether the token is valid or not
    public boolean validateToken(String token,String username){
        return (extractUsername(token).equals(username) && !isTokenExpired(token));
    }

}
