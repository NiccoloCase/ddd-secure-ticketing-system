package org.swe.helpers;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.Map;

public class JWTUtility {
    private final Key secretKey;
    private final long expirationTime; // Tempo di validità in millisecondi

    public JWTUtility(String secret, long expirationTime) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes()); // Genera la chiave a partire da una stringa segreta
        this.expirationTime = expirationTime;
    }

    /**
     * Crea un token JWT con i claims forniti
     *
     * @param claims Map con i dati da includere nel token
     * @return Stringa del token generato
     */
    public String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Valida un token JWT e restituisce i claims
     *
     * @param token Token JWT da validare
     * @return Claims contenuti nel token
     * @throws JwtException se il token non è valido
     */
    public Claims validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Estrae un claim specifico dal token
     *
     * @param token Token JWT
     * @param claimKey Chiave del claim da estrarre
     * @return Valore del claim
     */
    public Object getClaim(String token, String claimKey) {
        Claims claims = validateToken(token);
        return claims.get(claimKey);
    }
}
