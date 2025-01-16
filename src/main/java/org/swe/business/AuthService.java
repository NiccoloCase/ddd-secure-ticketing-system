package org.swe.business;

import java.util.HashMap;
import java.util.Map;

import org.swe.core.utils.JWTUtility;
import org.swe.core.utils.PasswordUtility;
import org.swe.model.User;

import io.jsonwebtoken.Claims;

public class AuthService {

    public AuthService() {
    }

    public String authenticate(String email, String password) {

        // User user = dao.findUserByEmail(email);
        User user = null;
        if (user == null) {
            return null;
        }

        if (!PasswordUtility.checkPassword(password, user.getPasswordHash())) {
            return null;
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());

        return JWTUtility.generateToken(claims);
    }

    public boolean invalidateToken(String token) {

        return true;
    }

    public Claims validateToken(String token) {
        // if (blacklistedTokens.contains(token)) {
        // return null;
        // }
        return JWTUtility.validateToken(token);
    }

    public void signupUser(String name, String email, String rawPassword) {
        // - Validate
        // - Hash password
        // - DB
    }

}
