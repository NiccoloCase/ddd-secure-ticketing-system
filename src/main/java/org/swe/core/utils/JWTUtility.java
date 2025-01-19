package org.swe.core.utils;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.swe.core.Config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;


public class JWTUtility {
    private static final long expirationTime = 3600000; // 1 hour

    /**
     * Generate a JWT token
     */
    public static String generateToken(Map<String, Object> claims) {

        SecretKey secretKey = Keys.hmacShaKeyFor(Config.JWT_SECRET.getBytes());

        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey)
                .compact();
    }

    /**
     * Validate a JWT token
     * returns:
     * - null if the token is invalid
     * - the claims if the token is valid
     */
    public static Claims validateToken(String token) {
        SecretKey secretKey = Keys.hmacShaKeyFor(Config.JWT_SECRET.getBytes());

        try {
            JwtParser parser = Jwts.parser()
                    .verifyWith(secretKey)
                    .build();
            Object payload = parser.parse(token).getPayload();

            if (payload instanceof Claims) {
                return (Claims) payload;
            }
        } catch (ExpiredJwtException | MalformedJwtException | SecurityException | IllegalArgumentException e) {
            System.err.println("Token validation failed: " + e.getMessage());
            return null;
        }
        return null;
    }
}

