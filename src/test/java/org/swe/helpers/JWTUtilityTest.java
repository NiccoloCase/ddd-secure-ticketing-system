package org.swe.helpers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JWTUtilityTest {
    @BeforeAll
    static void setUp() {
        Config.init();
    }


    // GENERATE TOKEN
    @Test
    void testGenerateToken() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", "testuser");
        claims.put("role", "admin");

        String token = JWTUtility.generateToken(claims);

        assertNotNull(token, "Generated token should not be null");
        assertFalse(token.isEmpty(), "Generated token should not be empty");
    }

    // VALIDATE TOKEN - 1) VALID TOKEN
    @Test
    void testValidateToken_ValidToken() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", "testuser");
        claims.put("role", "admin");

        String token = JWTUtility.generateToken(claims);
        Claims result = JWTUtility.validateToken(token);

        assertNotNull(result, "Claims should not be null for a valid token");
        assertEquals("testuser", result.get("username"), "Username claim should match");
        assertEquals("admin", result.get("role"), "Role claim should match");
    }

    // VALIDATE TOKEN - 2) EXPIRED TOKEN
    @Test
    void testValidateToken_ExpiredToken() throws InterruptedException {
        SecretKey customKey = Keys.hmacShaKeyFor("testSecretKey12345678901234567890".getBytes());
        long shortExpirationTime = 500; // .5 second

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", "testuser");

        String token = Jwts.builder()
                .claims(claims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + shortExpirationTime))
                .signWith(customKey)
                .compact();

        // Waiting for the token to expire
        Thread.sleep(600);

        // When
        Claims result = JWTUtility.validateToken(token);

        // Then
        assertNull(result, "Expired token should return null");
    }


    // VALIDATE TOKEN - 3) INVALID TOKEN
    @Test
    void testValidateToken_InvalidToken() {
        // Given
        String invalidToken = "invalid.token.signature";

        // When
        Claims result = JWTUtility.validateToken(invalidToken);

        // Then
        assertNull(result, "Invalid token should return null");
    }
}
