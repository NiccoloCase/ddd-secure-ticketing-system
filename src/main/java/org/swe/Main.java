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

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", "john.doe");
        claims.put("role", "admin");

        String token = JWTUtility.generateToken(claims);
        System.out.println("Generated Token: " + token);

        // Esempio di validazione e lettura dei claims
        try {
            Object parsedClaims = JWTUtility.validateToken(token);
            System.out.println("Token valid. Claims: " + parsedClaims);
        } catch (JwtException e) {
            System.out.println("Invalid token: " + e.getMessage());
        }


    }
}