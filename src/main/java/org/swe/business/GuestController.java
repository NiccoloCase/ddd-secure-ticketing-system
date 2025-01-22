package org.swe.business;

import org.swe.core.DAO.EventDAO;
import org.swe.core.DAO.TicketDAO;
import org.swe.core.DAO.UserDAO;
import org.swe.core.DTO.BuyTicketDTO;
import org.swe.core.DTO.ScanStaffVerificationCodeDTO;
import org.swe.core.exceptions.BadRequestException;
import org.swe.core.exceptions.NotFoundException;
import org.swe.core.utils.JWTUtility;
import org.swe.model.Event;
import org.swe.model.PaymentContext;
import org.swe.model.PaymentStrategy;
import org.swe.model.PaymentStrategyFactory;
import org.swe.model.Ticket;
import org.swe.model.User;
import org.swe.model.VerifySession;
import org.swe.model.VerifySessionStatus;

import io.jsonwebtoken.Claims;

public class GuestController extends UserController {
    private final VerifySessionService verifySessionService;
    private final EventDAO eventDAO;
    private final TicketDAO ticketDAO;

    public GuestController(AuthService authHandler, VerifySessionService verifySessionService, EventDAO eventDAO,
            TicketDAO ticketDAO, UserDAO userDAO) {
        super(authHandler, userDAO);
        this.verifySessionService = verifySessionService;
        this.eventDAO = eventDAO;
        this.ticketDAO = ticketDAO;
    }

    public Ticket buyTicket(BuyTicketDTO dto, String token) {
        validationInterceptor(dto);
        User user = authInterceptor(token);
        Event event = eventDAO.getEventById(dto.getEventId());
        
        if (event == null) {
            throw new NotFoundException("Event not found");
        }
        
        int available = event.getTicketsAvailable();
        if (available < dto.getQuantity()) {
            throw new BadRequestException("Not enough tickets available.");
        }
        
        // PAYMENT
        PaymentContext paymentContext = new PaymentContext(); // Create strategy context
        PaymentStrategy paymentStrategy = PaymentStrategyFactory.getPaymentStrategy(dto.getPaymentMethod()); // Get strategy from factory
        paymentContext.setPaymentStrategy(paymentStrategy);
        boolean paymentSuccess = paymentContext.executePayment(event.getTicketPrice() * dto.getQuantity());
        
        if (!paymentSuccess) {
            throw new BadRequestException("Payment failed. Please try again.");
        }
        
        // Update the availability of tickets
        boolean success = eventDAO.updateEvent(
            event.getId(),
            event.getTitle(),
            event.getDescription(),
            event.getDate(),
            available - dto.getQuantity(),
            event.getTicketPrice()
        );
        
        if (!success) {
            // [rollback of payment]
            throw new BadRequestException("Failed to update event ticket availability.");
        }
        
        // Create the ticket and return it
        Ticket ticket = ticketDAO.createTicket(user.getId(), dto.getEventId(), dto.getQuantity());
        if (ticket == null) {
            // rollback
            eventDAO.updateEvent(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getDate(), available,  // <-- restore original availability
                event.getTicketPrice()
            );
            throw new RuntimeException("Failed to create ticket");
        }
        
        return ticket;
    }

    public boolean scanStaffVerificationCode(ScanStaffVerificationCodeDTO dto, String token) {
        User user = authInterceptor(token);

        String code = dto.getCode();
        if (code == null) {
            throw new BadRequestException("Invalid code.");
        }

        Claims claims = JWTUtility.validateToken(code);
        String sessionKey = (String) claims.get("sessionKey");

        if (sessionKey == null) {
            throw new BadRequestException("Invalid sessionKey.");
        }

        VerifySession session = verifySessionService.getFromSession(sessionKey);
        if (session == null) {
            throw new BadRequestException("Invalid session.");
        }

        Integer eventId = session.getEventId();
        if (eventId == null) {
            session.setStatus(VerifySessionStatus.INVALID);
            throw new BadRequestException("No eventId found in session.");
        }

        session.linkSessionToUser(user);

        return true;
    }
}
