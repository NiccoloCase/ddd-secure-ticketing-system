package org.swe.business;

import org.swe.core.auth.AuthHandler;

public class StaffController extends UserController {

    public StaffController(AuthHandler authHandler) {
        super(authHandler);
    }

    public boolean validateUserTicket() {
        // TODO: Gestione JWT, markAsUsed, ecc.
        return true;
    }

}
