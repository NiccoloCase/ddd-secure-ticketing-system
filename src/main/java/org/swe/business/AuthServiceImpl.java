package org.swe.business;

import java.util.HashMap;
import java.util.Map;

import org.swe.core.DAO.ConcreteUserDAO;
import org.swe.core.DAO.UserDAO;
import org.swe.core.exceptions.NotFoundException;
import org.swe.core.utils.JWTUtility;
import org.swe.core.utils.PasswordUtility;
import org.swe.model.User;

import io.jsonwebtoken.Claims;

public class AuthServiceImpl implements AuthService {

    private final UserDAO userDAO = new ConcreteUserDAO();

    public AuthServiceImpl() {
    }

    @Override
    public String authenticate(String email, String password) {
        
        User user = userDAO.findUserByEmail(email);
        if (user == null) {
            throw new NotFoundException("User not found.");
        }

        String passwordHash = user.getPasswordHash();

        // TODO use new user.isPasswordCorrect()
        if (!PasswordUtility.checkPassword(password, passwordHash)) {
            throw new NotFoundException("PasswordHash not found in database.");
        }
        if (!PasswordUtility.checkPassword(password, user.getPasswordHash())) {
            throw new NotFoundException("Invalid password.");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());

        return JWTUtility.generateToken(claims);
    }

    @Override
    public Claims validateToken(String token) {
        return JWTUtility.validateToken(token);
    }
    
}