package org.swe.business;

import org.swe.core.DAO.AdminDAO;
import org.swe.core.DAO.ConcreteAdminDAO;
import org.swe.core.DAO.ConcreteEventDAO;
import org.swe.core.DAO.ConcreteStaffDAO;
import org.swe.core.DAO.ConcreteTicketDAO;
import org.swe.core.DAO.ConcreteUserDAO;
import org.swe.core.DAO.EventDAO;
import org.swe.core.DAO.StaffDAO;
import org.swe.core.DAO.TicketDAO;
import org.swe.core.DAO.UserDAO;

public final class ApplicationManager {

    private final AuthService authService;
    private final VerifySessionService verifySessionService;
    private final EventDAO eventDAO;
    private final TicketDAO ticketDAO;
    private final UserDAO userDAO;
    private final AdminDAO adminDAO;
    private final StaffDAO staffDAO;
    private GuestController guestController = null;
    private StaffController staffController = null;
    private AdminController adminController = null;

    public ApplicationManager() {
        // DAOs
        eventDAO = new ConcreteEventDAO();
        ticketDAO = new ConcreteTicketDAO();
        userDAO = new ConcreteUserDAO();
        adminDAO = new ConcreteAdminDAO();
        staffDAO = new ConcreteStaffDAO();
        // services
        authService = new AuthServiceImpl();
        verifySessionService = new VerifySessionServiceImpl();
    }

    public GuestController getGuestController() {
        if (guestController == null) {
            guestController = new GuestController(authService, verifySessionService, eventDAO, ticketDAO, userDAO);
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
            adminController = new AdminController(authService, eventDAO, userDAO, adminDAO, staffDAO);
        }
        return adminController;
    }
}
