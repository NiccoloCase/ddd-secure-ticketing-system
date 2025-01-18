package org.swe.business;

import org.swe.core.DTO.GetVerificationSessionResultDTO;
import org.swe.core.DTO.StartVerificationSessionDTO;
import org.swe.core.exceptions.BadRequestException;
import org.swe.model.VerifySession;

public class StaffController extends UserController {

    private final VerifySessionService verifySessionService;

    public StaffController(AuthService authService, VerifySessionService verifySessionService) {
        super(authService);
        this.verifySessionService = verifySessionService;
    }

    public String startVerificationSession(StartVerificationSessionDTO payload) {
        VerifySession verifySession = new VerifySession(
                12, // TODO replace with actual staff id
                payload.getEventId(),
                null
        );
        return verifySessionService.addToSession(verifySession);
    }

    public void getVerificationSessionResult(GetVerificationSessionResultDTO payload) throws BadRequestException {

        // Auth
        // TODO

        // Get the session from the session key if it exists
        VerifySession verifySession;
        try {

            verifySession = verifySessionService.getFromSession(payload.getSessionKey());
        }
        catch (Exception e) {
            throw new BadRequestException("Invalid session key");
        }

        // Check if the session is for the current staff member
        // TODO

        // If the session is still pending or does not have a ticket id, return a message
        // TODO

        // Check if the session has an TICKET ID registered and fetch the ticket
        // TODO

        // Check if the ticket is valid
        // TODO


        // Create the code for the qr code


    }
}
