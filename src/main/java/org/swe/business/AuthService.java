package org.swe.business;

import org.swe.core.exceptions.UnauthorizedException;
import org.swe.model.User;

import io.jsonwebtoken.Claims;

public interface AuthService {
    String authenticateWithPassword(User user, String password) throws UnauthorizedException;
    String generateAccessToken(User user);
    Claims validateAccessToken(String token);
}