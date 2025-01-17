package org.swe.business;

import io.jsonwebtoken.Claims;

public interface AuthService {
    String authenticate(String email, String password);
    Claims validateToken(String token);
}