package org.swe.business;

public class UserController {

    private final AuthService authHandler;

    public UserController(AuthService authHandler) {
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
