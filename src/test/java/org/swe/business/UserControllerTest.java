package org.swe.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.swe.core.DTO.LoginDTO;
import org.swe.core.exceptions.UnauthorizedException;

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

        LoginDTO dto = new LoginDTO(email, password);

        when(mockAuthService.authenticate(email, password)).thenReturn(expectedToken);

        String actualToken = userController.login(dto);

        assertEquals(expectedToken, actualToken, "The token should match the expected one");
        verify(mockAuthService, times(1)).authenticate(email, password);

    }

    @Test
    public void testLoginFailure() {
        String email = "user@example.com";
        String password = "wrongPassword";

        LoginDTO dto = new LoginDTO(email, password);
        when(mockAuthService.authenticate(email, password)).thenReturn(null);
        assertThrows(UnauthorizedException.class, () -> userController.login(dto));

        verify(mockAuthService, times(1)).authenticate(email, password);

    }

}
