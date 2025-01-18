package org.swe.business;

import org.junit.jupiter.api.BeforeEach;
import static org.mockito.Mockito.mock;
import org.swe.core.DAO.AdminDAO;
import org.swe.core.DAO.EventDAO;
import org.swe.core.DAO.StaffDAO;
import org.swe.core.DAO.UserDAO;

public class AdminControllerTest {

    private AdminController adminController;
    private EventDAO mockEventDAO;
    private UserDAO mockUserDAO;
    private AdminDAO mockAdminDAO;
    private StaffDAO mockStaffDAO;
    private AuthService mockAuthService;

    @BeforeEach
    void setUp() {
        mockEventDAO = mock(EventDAO.class);
        mockUserDAO = mock(UserDAO.class);
        mockAdminDAO = mock(AdminDAO.class);
        mockStaffDAO = mock(StaffDAO.class);
        mockAuthService = mock(AuthService.class);

        adminController = new AdminController(mockAuthService, mockEventDAO, mockUserDAO, mockAdminDAO, mockStaffDAO);
    }
}
