package org.swe.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.swe.core.DAO.UserDAO;
import org.swe.core.DTO.CreateUserDTO;
import org.swe.core.DTO.LoginDTO;
import org.swe.core.exceptions.BadRequestException;
import org.swe.model.User;

class UserControllerTest {

    private AuthService authServiceMock;
    private UserDAO userDAOMock;
    private UserController userController;

    @BeforeEach
    void setUp() {
        authServiceMock = mock(AuthService.class);
        userDAOMock = mock(UserDAO.class);
        userController = new UserController(authServiceMock, userDAOMock);
    }

    @Test
    void loginShouldReturnTokenWhenValidCredentials() {
        String password = "password";
        LoginDTO dto = new LoginDTO("mario.rossi@gmail.com", password);
        User user = new User("Mario", "Rossi", User.hashPassword(password), "mario.rossi@gmail.com",1);

        when(userDAOMock.getUserByEmail(dto.email)).thenReturn(user);
        when(authServiceMock.authenticateWithPassword(user, dto.password)).thenReturn("token123");

        String token = userController.login(dto);

        assertEquals("token123", token);
        verify(userDAOMock).getUserByEmail(dto.email);
        verify(authServiceMock).authenticateWithPassword(user, dto.password);
    }

    @Test
    void loginShouldThrowBadRequestExceptionWhenUserNotFound() {
        LoginDTO dto = new LoginDTO("invalid@example.com", "password");

        when(userDAOMock.getUserByEmail(dto.email)).thenReturn(null);

        assertThrows(BadRequestException.class, () -> userController.login(dto));
        verify(userDAOMock).getUserByEmail(dto.email);
    }

    @Test
    void signupShouldReturnTokenWhenUserIsCreated() {
        String password = "password";
        CreateUserDTO dto = new CreateUserDTO("Mario", "Rossi", "mario.rossi@gmail.com", password);
        User newUser = new User("Mario", "Rossi", User.hashPassword(password), "mario.rossi@gmail.com",2);

        when(userDAOMock.createUser(dto.getName(), dto.getSurname(), dto.getPassword(), dto.getEmail()))
                .thenReturn(newUser);
        when(authServiceMock.generateAccessToken(newUser)).thenReturn("newToken123");

        String token = userController.signup(dto);

        assertEquals("newToken123", token);
        verify(userDAOMock).createUser(dto.getName(), dto.getSurname(), dto.getPassword(), dto.getEmail());
        verify(authServiceMock).generateAccessToken(newUser);
    }

    @Test
    void signupShouldThrowBadRequestExceptionWhenUserAlreadyExists() {
        CreateUserDTO dto = new CreateUserDTO("Mario", "Rossi", "password", "mario.rossi@gmail.com");

        when(userDAOMock.createUser(dto.getName(), dto.getSurname(), dto.getPassword(), dto.getEmail()))
                .thenThrow(new RuntimeException("User already exists"));

        assertThrows(BadRequestException.class, () -> userController.signup(dto));
    }

    @Test
    void signupShouldThrowBadRequestExceptionWhenEmailFormatIsInvalid() {
        CreateUserDTO dto = new CreateUserDTO("Mario", "Rossi",  "mario.rossi", "password");
        assertThrows(BadRequestException.class, () -> userController.signup(dto));

    }
}
