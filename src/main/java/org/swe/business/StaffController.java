package org.swe.business;

import org.swe.core.DAO.UserDAO;
import org.swe.core.DTO.GetVerificationSessionResultDTO;
import org.swe.core.DTO.StartVerificationSessionDTO;
import org.swe.core.exceptions.BadRequestException;
import org.swe.model.User;
import org.swe.model.VerifySession;

public class StaffController extends UserController {

    private final VerifySessionService verifySessionService;

    public StaffController(AuthService authService, VerifySessionService verifySessionService, UserDAO userDAO) {
        super(authService, userDAO);
        this.verifySessionService = verifySessionService;
    }

    public String startVerificationSession(StartVerificationSessionDTO payload, String token) {
        validationInterceptor(payload);
        User user = authInterceptor(token);

        VerifySession verifySession = new VerifySession(
                user.getId(),
                payload.getEventId(),
                null
        );
        return verifySessionService.addToSession(verifySession);
    }

    public void getVerificationSessionResult(GetVerificationSessionResultDTO payload, String token) throws BadRequestException {
        validationInterceptor(payload);
        User user = authInterceptor(token);
        VerifySession verifySession;
        
        // Get the session from the session key if it exists
        try{
            verifySession = verifySessionService.getFromSession(payload.getSessionKey());
        } catch (Exception e) {
            throw new BadRequestException("Session does not exist");
        }
        

        // Check if the session is for the current staff member
        if(verifySession.getStaffId() != user.getId()){
            throw new BadRequestException("Session does not belong to the current staff member");
        }

        // If the session is still pending or does not have a ticket id, return a message
        // TODO

        // Check if the session has an TICKET ID registered and fetch the ticket
        // TODO
        

        // Check if the ticket is valid
        // TODO


        // Create the code for the qr code


    }
}
