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

    /**
     * Authenticate the user with the password:
     * - Check if the password is correct
     * - Generate a JWT token
     * @param user
     * @param password
     * @return
     * @throws UnauthorizedException
     */
    @Override
    public String authenticateWithPassword(User user, String password) throws UnauthorizedException {
        if(!user.isPasswordCorrect(password)){
            throw new UnauthorizedException("Invalid credentials");
        }
        return this.generateAccessToken(user);
    }


    /**
     * Validate the token and return the user id
     *
     * @param token JWT token
     * @return the user id
     */
    @Override
    public Integer validateAccessToken(String token) {
        Claims  payload = JWTUtility.validateToken(token);
        if(payload == null) return null;
        return payload.get("userId", Integer.class);
    }

    /**
     * Generate a JWT token for the user
     * @param user
     * @return
     */
    @Override
    public String generateAccessToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        return JWTUtility.generateToken(claims);
    }
}