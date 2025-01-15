package org.swe.core.auth;

import java.util.HashMap;
import java.util.Map;

import org.swe.core.utils.JWTUtility;
import org.swe.core.utils.PasswordUtility;

public class AuthHandler {

    public AuthHandler() {
    }

    public String authenticate(String email, String password) {

        // TODO implementare il fetch dell'utente dal db e la verifica della password

        // String passwordHash = dbManager.getPasswordHash(email);
        String role = "user";
        String passwordHash = PasswordUtility.hashPassword(password);
        System.out.println("Generated password hash: " + passwordHash);
        boolean isPasswordValid = PasswordUtility.checkPassword(password, passwordHash);

        if (isPasswordValid) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("email", email);
            claims.put("role", role);

            String token = JWTUtility.generateToken(claims);
            System.out.println("Generated token: " + token);
            return token;
        }

        return null;
    }

    public boolean invalidateToken(String token) {
        // TODO implementare la blacklist dei token
        return true;
    }

    public boolean validateToken(String token) {
        // TODO implementare la verifica del token
        return true;
    }

}
