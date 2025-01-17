package org.swe.business;

import org.swe.core.DAO.ConcreteEventDAO;
import org.swe.core.DAO.ConcreteUserDAO;
import org.swe.core.DAO.EventDAO;
import org.swe.core.DAO.UserDAO;

public final class ApplicationManager {

    private final AuthService authService;
    private final VerifySessionService verifySessionService;
    private final EventDAO eventDAO = new ConcreteEventDAO();
    private final UserDAO userDAO = new ConcreteUserDAO();
    private GuestController guestController = null;
    private StaffController staffController = null;
    private AdminController adminController = null;

    public ApplicationManager() {
        authService = new AuthServiceImpl();
        verifySessionService = new VerifySessionServiceImpl();
    }

    public GuestController getGuestController() {
        if (guestController == null) {
            guestController = new GuestController(authService);
        }
        return guestController;
    }

    public StaffController getStaffController() {
        if (staffController == null) {
            staffController = new StaffController(authService, verifySessionService);
        }
        return staffController;
    }

    public AdminController getAdminController() {
        if (adminController == null) {
            adminController = new AdminController(authService, eventDAO, userDAO);
        }
        return adminController;
    }

}
