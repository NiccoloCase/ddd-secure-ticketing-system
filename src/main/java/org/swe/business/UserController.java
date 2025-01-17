package org.swe.business;

import org.swe.core.DAO.ConcreteUserDAO;
import org.swe.core.DAO.UserDAO;
import org.swe.core.utils.JWTUtility;
import org.swe.model.User;

import io.jsonwebtoken.Claims;

public class UserController {

    private final AuthService authService;
    private final UserDAO userDAO = new ConcreteUserDAO();

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

    public void signup() {

        // TODO cosa ritornerà? un token? quali sono i parametri?

    }

    public boolean validateToken(String token) {
        return authService.validateToken(token) != null;
    }

    protected User validateInterceptor(String token) {
        Claims claims = JWTUtility.validateToken(token);
        if (claims == null) {
            throw new IllegalArgumentException("Invalid or expired token.");
        }
        Integer userId = claims.get("userId", Integer.class);
        if (userId == null) {
            throw new IllegalArgumentException("No userId found in token.");
        }
        User user = userDAO.getUser(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }
        return user;
    }
}
