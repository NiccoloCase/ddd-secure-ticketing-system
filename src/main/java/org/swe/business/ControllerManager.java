package org.swe.business;

import org.swe.core.auth.AuthHandler;

public final class ControllerManager {

    // FIXME: 1 solo authHandler per tutti i controller?
    private static final AuthHandler authHandler = new AuthHandler();
    private static GuestController guestController = null;
    private static StaffController staffController = null;
    private static AdminController adminController = null;

    private ControllerManager() {
    }

    public static synchronized GuestController getGuestController() {
        if (guestController == null) {
            guestController = new GuestController(authHandler);
        }
        return guestController;
    }

    public static synchronized StaffController getStaffController() {
        if (staffController == null) {
            staffController = new StaffController(authHandler);
        }
        return staffController;
    }

    public static synchronized AdminController getAdminController() {
        if (adminController == null) {
            adminController = new AdminController(authHandler);
        }
        return adminController;
    }

}
