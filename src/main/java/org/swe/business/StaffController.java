package org.swe.business;

public class StaffController extends UserController {

    public StaffController(AuthService authHandler) {
        super(authHandler);
    }

    public boolean validateUserTicket() {
        // TODO: Gestione JWT, markAsUsed, ecc.
        return true;
    }

}
