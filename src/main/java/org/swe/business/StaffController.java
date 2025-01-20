package org.swe.business;

import org.swe.core.DAO.UserDAO;

import java.util.List;

import org.swe.core.DAO.TicketDAO;
import org.swe.core.DTO.GetVerificationSessionResultDTO;
import org.swe.core.DTO.StartVerificationSessionDTO;
import org.swe.core.exceptions.BadRequestException;
import org.swe.model.Staff;
import org.swe.model.Ticket;
import org.swe.model.User;
import org.swe.model.VerificationSessionResult;
import org.swe.model.VerifySession;
import org.swe.model.VerifySessionStatus;

public class StaffController extends UserController {

    private final VerifySessionService verifySessionService;
    private final TicketDAO ticketDAO;

    public StaffController(AuthService authService, VerifySessionService verifySessionService, UserDAO userDAO, TicketDAO ticketDAO) {
        super(authService, userDAO);
        this.verifySessionService = verifySessionService;
        this.ticketDAO = ticketDAO;
    }

    public String startVerificationSession(StartVerificationSessionDTO payload, String token) {
        validationInterceptor(payload);
        User staff = authInterceptor(token);

        VerifySession verifySession = new VerifySession(
                staff.getId(),
                payload.getEventId()
        );
        return verifySessionService.addToSession(verifySession);
    }

    public VerificationSessionResult validateVerificationSession(GetVerificationSessionResultDTO payload, String token) throws BadRequestException {
        validationInterceptor(payload);
        Staff staff = (Staff) authInterceptor(token);
        VerifySession verifySession = null;
        
        // Get the session from the session key if it exists
        try{
            verifySession = verifySessionService.getFromSession(payload.getSessionKey());
        } catch (Exception e) {
            throw new BadRequestException("Session does not exist");
        }
        

        // Check if the session is for the current staff member
        if(verifySession.getStaffId() != staff.getId()){
            throw new BadRequestException("Session does not belong to the current staff member");
        }

        // Check if the session is already validated
        if(verifySession.getStatus()==VerifySessionStatus.VALIDATED){
            throw new BadRequestException("Session is already validated");
        }

        // Check if the session is still pending or if there is no guest linked to the session
        if(!(verifySession.getStatus()==VerifySessionStatus.PENDING) || verifySession.getGuest()==null){
            throw new BadRequestException("Session is still pending or there is no guest linked to the session");
        }

        // Get all the tickets for the user and the event
        List<Ticket> tickets = ticketDAO.getTicketsByUserAndEvent(verifySession.getGuest().getId(), verifySession.getEventId());
        
        // Check if the ticket is valid
        if (tickets.isEmpty()) {
            verifySession.setStatus(VerifySessionStatus.INVALID);
            throw new BadRequestException("No tickets found for the user and event");
        }

        // Verify the session
        int validTickets = 0;
        int i;
        for (i = 0; i < tickets.size(); i++) {
            if (!tickets.get(i).isUsed()) {
                try{
                    ticketDAO.setTicketUsed(tickets.get(i).getId());
                    validTickets+=tickets.get(i).getQuantity();
                } catch (Exception e) {
                    i--;
                }
            }
        }
        
        // Check if there are any valid tickets
        if(validTickets == 0){
            verifySession.setStatus(VerifySessionStatus.INVALID);
            throw new BadRequestException("No valid tickets found for the user and event");
        }

        verifySessionService.validateSession(payload.getSessionKey());
        return new VerificationSessionResult(
                verifySession.getGuest().getIdentity()
                , verifySession.getStatus(), validTickets);
    }
}
