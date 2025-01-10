package org.swe;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.swe.helpers.Config;
import org.swe.helpers.JWTUtility;

import java.util.HashMap;
import java.util.Map;




public class Main {
    public static void main(String[] args) {


        Config.init();

        System.out.println("Hello world!");


        // Configura la utility
        String secret = "supersecretkey12345678901234567890"; // Deve essere lunga almeno 32 byte
        long expirationTime = 3600000; // 1 ora (in millisecondi)

        JWTUtility jwtUtility = new JWTUtility(secret, expirationTime);

        // Esempio di creazione di un token
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", "john.doe");
        claims.put("role", "admin");

        String token = jwtUtility.generateToken(claims);
        System.out.println("Generated Token: " + token);

        // Esempio di validazione e lettura dei claims
        try {
            Claims parsedClaims = jwtUtility.validateToken(token);
            System.out.println("Token valid. Claims: " + parsedClaims);
        } catch (JwtException e) {
            System.out.println("Invalid token: " + e.getMessage());
        }

        // Estrazione di un claim specifico
        String username = (String) jwtUtility.getClaim(token, "username");
        System.out.println("Username from token: " + username);
    }
}