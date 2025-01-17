package org.swe.business;

import org.junit.jupiter.api.BeforeEach;
import static org.mockito.Mockito.mock;
import org.swe.core.DAO.EventDAO;
import org.swe.core.DAO.UserDAO;

public class AdminControllerTest {

    private AdminController adminController;
    private EventDAO mockEventDAO;
    private UserDAO mockUserDAO;
    private AuthService mockAuthService;

    @BeforeEach
    void setUp() {
        mockEventDAO = mock(EventDAO.class);
        mockUserDAO = mock(UserDAO.class);
        mockAuthService = mock(AuthService.class);

        adminController = new AdminController(mockAuthService);
        adminController.setEventDAO(mockEventDAO);
        adminController.setUserDAO(mockUserDAO);
    }
}
