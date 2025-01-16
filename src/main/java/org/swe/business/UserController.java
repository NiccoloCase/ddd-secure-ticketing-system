package org.swe.business;

public class UserController {

    private final AuthService authService;

    public UserController(AuthService authService) {
        this.authService = authService;
    }

    public String login(String email, String password) {
        String token = authService.authenticate(email, password);

        if (token != null) {
            return token;
        } else {
            throw new IllegalArgumentException("Invalid email or password");
        }
    }

    public boolean logout(String token) {
        return authService.invalidateToken(token);
    }

    public void signup() {

        // TODO cosa ritornerà? un token? quali sono i parametri?

    }

    public boolean validateToken(String token) {
        return authService.validateToken(token) != null;
    }
}
