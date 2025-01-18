package org.swe.business;

import io.jsonwebtoken.Claims;
import org.swe.core.exceptions.UnauthorizedException;
import org.swe.model.User;

public interface AuthService {
    String authenticateWithPassword(User user, String password) throws UnauthorizedException;
    String generateAccessToken(User user);
    Claims validateAccessToken(String token);
}