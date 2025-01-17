package org.swe.business;

public final class ApplicationManager {

    private final AuthService authService;
    private GuestController guestController = null;
    private StaffController staffController = null;
    private AdminController adminController = null;

    public ApplicationManager() {
        authService = new AuthServiceImpl();
    }

    public GuestController getGuestController() {
        if (guestController == null) {
            guestController = new GuestController(authService);
        }
        return guestController;
    }

    public StaffController getStaffController() {
        if (staffController == null) {
            staffController = new StaffController(authService);
        }
        return staffController;
    }

    public AdminController getAdminController() {
        if (adminController == null) {
            adminController = new AdminController(authService);
        }
        return adminController;
    }

}
