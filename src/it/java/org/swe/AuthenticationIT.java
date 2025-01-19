package org.swe;

import org.junit.jupiter.api.*;
import org.swe.business.ApplicationManager;
import org.swe.business.UserController;
import org.swe.core.DBM.DBManager;
import org.swe.core.DTO.CreateUserDTO;
import org.swe.core.DTO.LoginDTO;
import org.swe.core.exceptions.BadRequestException;

import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthenticationIT {

    static UserController userController;

    @BeforeAll
    public static void setUp() {
        ApplicationManager app = new ApplicationManager();
        userController = app.getGuestController();

        DBManager.getInstance().clearTables();
        System.out.println("Tables cleared");


    }

    @AfterAll
    public static void tearDown() {
        DBManager.getInstance().clearTables();
    }



    @Test
    @Order(1)
    public void signupShouldThrowsExceptionIfValidationFails() {
        CreateUserDTO user = new CreateUserDTO("Mario", "Rossi", "mario.rossi", "password");
        assertThrows(BadRequestException.class, () -> userController.signup(user));
    }

    @Test
    @Order(2)
    public void signupShouldReturnTokenIfSuccess() {
        CreateUserDTO user = new CreateUserDTO("Mario", "Rossi", "rossi@gmail.com", "password");
        String token = userController.signup(user);
        assertNotNull(token);
    }

    @Test
    @Order(3)
    public void signupShouldFailIfEmailAlreadyExists() {
        CreateUserDTO user1 = new CreateUserDTO("Luigi", "Rossi", "rossi@gmail.com", "password");
        assertThrows(BadRequestException.class, () -> userController.signup(user1));
    }

    @Test
    @Order(4)
    public void loginShouldReturnTokenIfSuccess() {
        LoginDTO user = new LoginDTO("rossi@gmail.com", "password");
        String token = userController.login(user);
        assertNotNull(token);
    }

    @Test
    @Order(5)
    public void loginShouldFailIfUserDoesNotExist() {
        LoginDTO user = new LoginDTO("mario@gmail.com", "password");
        assertThrows(BadRequestException.class, () -> userController.login(user));
    }

    @Test
    @Order(6)
    public void afterLoginWhoamiShouldReturnNameAndSurname() {
        LoginDTO user = new LoginDTO("rossi@gmail.com", "password");
        String token = userController.login(user);
        String nameAndSurname = userController.whoami(token);
        assertEquals("Mario Rossi", nameAndSurname);
        }
}
