package org.swe.business;

import java.util.HashMap;
import java.util.Map;

import org.swe.core.utils.JWTUtility;
import org.swe.core.utils.PasswordUtility;
import org.swe.model.User;

import io.jsonwebtoken.Claims;

public class AuthServiceImpl implements AuthService {

    public AuthServiceImpl() {
    }

    @Override
    public String authenticate(String email, String password) {
        // User user = dao.findUserByEmail(email);
        User user = null; // Simula il DAO per ora
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

    @Override
    public Claims validateToken(String token) {
        // if (blacklistedTokens.contains(token)) {
        // return null;
        // }
        return JWTUtility.validateToken(token);
    }
}