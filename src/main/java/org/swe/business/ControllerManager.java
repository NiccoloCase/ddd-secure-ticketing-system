package org.swe.business;

public final class ControllerManager {

    // FIXME: 1 solo authHandler per tutti i controller?
    private static final AuthService authService = new AuthService();
    private static GuestController guestController = null;
    private static StaffController staffController = null;
    private static AdminController adminController = null;

    private ControllerManager() {
    }

    public static synchronized GuestController getGuestController() {
        if (guestController == null) {
            guestController = new GuestController(authService);
        }
        return guestController;
    }

    public static synchronized StaffController getStaffController() {
        if (staffController == null) {
            staffController = new StaffController(authService);
        }
        return staffController;
    }

    public static synchronized AdminController getAdminController() {
        if (adminController == null) {
            adminController = new AdminController(authService);
        }
        return adminController;
    }

}
