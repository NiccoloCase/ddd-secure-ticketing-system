package org.swe.business;

import java.util.HashMap;
import java.util.Map;
import org.swe.core.exceptions.UnauthorizedException;
import org.swe.core.utils.JWTUtility;
import org.swe.model.User;
import io.jsonwebtoken.Claims;

public class AuthServiceImpl implements AuthService {

    public AuthServiceImpl() {
    }

    @Override
    public String authenticateWithPassword(User user, String password) throws UnauthorizedException {
        if(!user.isPasswordCorrect(password)){
            throw new UnauthorizedException("Invalid credentials");
        }
        return this.generateAccessToken(user);
    }

    @Override
    public Claims validateAccessToken(String token) {
        return JWTUtility.validateToken(token);
    }

    @Override
    public String generateAccessToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        return JWTUtility.generateToken(claims);
    }
}