package com.example.sbp.common.component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenManager {
    private final Key key;
    private final long validityInMilliseconds;

    public JwtTokenManager(@Value("${application.jwt.secret}") String secretKey,
                           @Value("${application.jwt.expiration}") long validityInMilliseconds) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.validityInMilliseconds = validityInMilliseconds;
    }

    public String issue(long userId, String userName) {
        var claims = Jwts.claims().setSubject(userName);
        claims.put("uid", userId);

        var issuedAt = new Date();
        var validUntil = new Date(issuedAt.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issuedAt)
                .setExpiration(validUntil)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isValid(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Claims parse(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
}