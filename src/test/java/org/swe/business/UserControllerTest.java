package org.swe.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private AuthService mockAuthService;
    private UserController userController;

    @BeforeEach
    public void setUp() {
        mockAuthService = Mockito.mock(AuthService.class);
        userController = new UserController(mockAuthService);
    }

    @Test
    public void testLoginSuccess() {
        String email = "user@example.com";
        String password = "password";
        String expectedToken = "validToken";

        when(mockAuthService.authenticate(email, password)).thenReturn(expectedToken);

        String actualToken = userController.login(email, password);

        assertEquals(expectedToken, actualToken, "The token should match the expected one");
        verify(mockAuthService, times(1)).authenticate(email, password);

    }

    @Test
    public void testLoginFailure() {
        String email = "user@example.com";
        String password = "wrongPassword";

        when(mockAuthService.authenticate(email, password)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> userController.login(email, password));

        verify(mockAuthService, times(1)).authenticate(email, password);

    }

    @Test
    public void testLogoutSuccess() {
        String token = "validToken";

        when(mockAuthService.invalidateToken(token)).thenReturn(true);

        boolean result = userController.logout(token);

        assertTrue(result, "The token should be invalidated");

        verify(mockAuthService, times(1)).invalidateToken(token);
    }

    @Test
    public void testLogoutFailure() {
        String token = "invalidToken";

        when(mockAuthService.invalidateToken(token)).thenReturn(false);

        boolean result = userController.logout(token);

        assertFalse(result, "The logout should fail");

        verify(mockAuthService, times(1)).invalidateToken(token);
    }

}
