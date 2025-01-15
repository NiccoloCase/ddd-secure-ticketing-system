package org.swe.business;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.swe.core.auth.AuthHandler;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    private AuthHandler mockAuthHandler;
    private UserController userController;

    @BeforeEach
    public void setUp() {
        mockAuthHandler = Mockito.mock(AuthHandler.class);
        userController = new UserController(mockAuthHandler);
    }

    @Test
    public void testLoginSuccess() {
        String email = "user@example.com";
        String password = "password";
        String expectedToken = "validToken";

        when(mockAuthHandler.authenticate(email, password)).thenReturn(expectedToken);

        String actualToken = userController.login(email, password);

        assertEquals(expectedToken, actualToken, "The token should match the expected one");
        verify(mockAuthHandler, times(1)).authenticate(email, password);

    }

    @Test
    public void testLoginFailure() {
        String email = "user@example.com";
        String password = "wrongPassword";

        when(mockAuthHandler.authenticate(email, password)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> userController.login(email, password));

        verify(mockAuthHandler, times(1)).authenticate(email, password);

    }

    @Test
    public void testLogoutSuccess() {
        String token = "validToken";

        when(mockAuthHandler.invalidateToken(token)).thenReturn(true);

        boolean result = userController.logout(token);

        assertTrue(result, "The token should be invalidated");

        verify(mockAuthHandler, times(1)).invalidateToken(token);
    }

    @Test
    public void testLogoutFailure() {
        String token = "invalidToken";

        when(mockAuthHandler.invalidateToken(token)).thenReturn(false);

        boolean result = userController.logout(token);

        assertFalse(result, "The logout should fail");

        verify(mockAuthHandler, times(1)).invalidateToken(token);
    }
}
