package org.swe.business;

import java.util.List;

import org.swe.core.DAO.EventDAO;
import org.swe.core.DAO.TicketDAO;
import org.swe.core.DAO.UserDAO;
import org.swe.core.DTO.BuyTicketDTO;
import org.swe.core.DTO.ScanStaffVerificationCodeDTO;
import org.swe.core.exceptions.BadRequestException;
import org.swe.core.exceptions.NotFoundException;
import org.swe.core.utils.JWTUtility;
import org.swe.model.ApplePayPayment;
import org.swe.model.CreditCardPayment;
import org.swe.model.Event;
import org.swe.model.GooglePayPayment;
import org.swe.model.PaymentContext;
import org.swe.model.PaymentStrategy;
import org.swe.model.Ticket;
import org.swe.model.User;
import org.swe.model.VerifySession;

import io.jsonwebtoken.Claims;

public class GuestController extends UserController {
    private VerifySessionService verifySessionService;
    private EventDAO eventDAO;
    private TicketDAO ticketDAO;
    private UserDAO userDAO;

    public GuestController(AuthService authHandler, VerifySessionService verifySessionService, EventDAO eventDAO,
            TicketDAO ticketDAO, UserDAO userDAO) {
        super(authHandler);
        this.verifySessionService = verifySessionService;
        this.eventDAO = eventDAO;
        this.ticketDAO = ticketDAO;
        this.userDAO = userDAO;
    }

    public Ticket buyTicket(BuyTicketDTO dto, String token) {

        User user = authInterceptor(token);

        Event event = eventDAO.getEventById(dto.getEventId());
        if (event == null) {
            throw new NotFoundException("Event not found");
        }

        int available = event.getTicketsAvailable();
        if (available < dto.getQuantity()) {
            throw new BadRequestException("Not enough tickets available.");
        }

        PaymentContext paymentContext = new PaymentContext();
        PaymentStrategy paymentStrategy;

        switch (dto.getPaymentMethod()) { // Utilizzo dell'enum
            case GOOGLE_PAY:
                paymentStrategy = new GooglePayPayment();
                break;
            case CREDIT_CARD:
                paymentStrategy = new CreditCardPayment();
                break;
            case APPLE_PAY:
                paymentStrategy = new ApplePayPayment();
                break;
            default:
                throw new BadRequestException("Unsupported payment method.");
        }

        paymentContext.setPaymentStrategy(paymentStrategy);
        boolean paymentSuccess = paymentContext.executePayment(event.getTicketPrice() * dto.getQuantity());

        if (!paymentSuccess) {
            throw new BadRequestException("Payment failed. Please try again.");
        }

        boolean success = eventDAO.updateEvent(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getDate(),
                available - dto.getQuantity(), // decremento
                event.getTicketPrice());
        if (!success) {
            throw new BadRequestException("Failed to update event ticket availability.");
        }

        Ticket newTicket = ticketDAO.createTicket(user.getId(), dto.getEventId(), dto.getQuantity());
        if (newTicket == null) {
            throw new BadRequestException("Failed to create ticket.");
        }

        return newTicket;
    }

    public boolean scanStaffVerificationCodeDTO(ScanStaffVerificationCodeDTO dto, String token) {
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
            throw new BadRequestException("No eventId found in session.");
        }

        List<Ticket> userTickets = ticketDAO.getTicketsByUserAndEvent(user.getId(), eventId);
        if (userTickets == null || userTickets.isEmpty()) {
            verifySessionService.rejectSession(sessionKey);
            return false;
        }

        Ticket validTicket = null;
        for (Ticket t : userTickets) {
            if (!t.isUsed()) {
                validTicket = t;
                break; // TODO: gestire il caso in cui ci siano più ticket validi
            }
        }
        if (validTicket == null) {
            verifySessionService.rejectSession(sessionKey);
            return false;
        }

        boolean updated = ticketDAO.setTicketUsed(validTicket.getId());
        if (!updated) {
            verifySessionService.rejectSession(sessionKey);
            throw new RuntimeException("Failed to mark ticket as used in DB.");
        }

        verifySessionService.verifySession(sessionKey, validTicket.getId());

        return true;

    }

}
