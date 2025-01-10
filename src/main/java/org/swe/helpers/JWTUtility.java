package org.swe.helpers;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.Map;


public class JWTUtility {
    private static final long expirationTime = 3600000; // 1 ora (in millisecondi)
    private static final Key secretKey = Keys.hmacShaKeyFor(Config.JWT_SECRET.getBytes());

    /**
     * Generate a JWT token
     */
    public static String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Validate a JWT token
     */
    public static Claims validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Extract a claim from a JWT token
     */
    public static Object getClaim(String token, String claimKey) {
        Claims claims = validateToken(token);
        return claims.get(claimKey);
    }
}
