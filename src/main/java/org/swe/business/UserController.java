package org.swe.business;

import org.swe.core.auth.AuthHandler;

public class UserController {

    private final AuthHandler authHandler;

    public UserController(AuthHandler authHandler) {
        this.authHandler = authHandler;
    }

    public String login(String email, String password) {
        String token = authHandler.authenticate(email, password);

        if (token != null) {
            return token;
        } else {
            throw new IllegalArgumentException("Invalid email or password");
        }
    }

    public boolean logout(String token) {
        return authHandler.invalidateToken(token);
    }

    public void signup() {

        // TODO cosa ritornerà? un token? quali sono i parametri?

    }
}
