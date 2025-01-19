package org.swe.business;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.swe.core.DAO.EventDAO;
import org.swe.core.DAO.TicketDAO;
import org.swe.core.DAO.UserDAO;
import org.swe.core.DTO.BuyTicketDTO;
import org.swe.core.exceptions.BadRequestException;
import org.swe.core.exceptions.NotFoundException;
import org.swe.model.Event;
import org.swe.model.PaymentContext;
import org.swe.model.PaymentMethod;
import org.swe.model.PaymentStrategy;
import org.swe.model.Ticket;
import org.swe.model.User;

class GuestControllerTest {

    private GuestController guestController;
    private AuthService mockAuthService;
    private VerifySessionService mockVerifySessionService;
    private EventDAO mockEventDAO;
    private TicketDAO mockTicketDAO;
    private UserDAO mockUserDAO;

    @BeforeEach
    void setUp() {
        mockAuthService = mock(AuthService.class);
        mockVerifySessionService = mock(VerifySessionService.class);
        mockEventDAO = mock(EventDAO.class);
        mockTicketDAO = mock(TicketDAO.class);
        mockUserDAO = mock(UserDAO.class);

        guestController = new GuestController(mockAuthService, mockVerifySessionService, mockEventDAO, mockTicketDAO, mockUserDAO);

        // Mock user authentication
        when(mockAuthService.validateAccessToken("token")).thenReturn(1);
        when(mockUserDAO.getUserById(1)).thenReturn(new User("name", "surname", "password", "email", 1));
    }

    @Nested
    class BuyTicketTests {

        @Test
        void buyTicketShouldReturnTicketIfPurchaseIsSuccessful() {
            BuyTicketDTO dto = new BuyTicketDTO();
            dto.setEventId(1);
            dto.setQuantity(2);
            dto.setPaymentMethod(PaymentMethod.CREDIT_CARD);

            Event event = new Event.Builder()
                .setId(1)
                .setTitle("title")
                .setDescription("description")
                .setDate(Date.from(LocalDate.of(2025, 12, 12).atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .setTicketsAvailable(5)
                .setTicketPrice(10)
                .build();
            when(mockEventDAO.getEventById(1)).thenReturn(event);

            PaymentStrategy paymentStrategy = mock(PaymentStrategy.class);
            when(paymentStrategy.processPayment(20)).thenReturn(true);

            PaymentContext paymentContext = new PaymentContext();
            paymentContext.setPaymentStrategy(paymentStrategy);

            when(mockEventDAO.updateEvent(
                    1, 
                    event.getTitle(), 
                    event.getDescription(), 
                    event.getDate(), 
                    3, 
                    10))
                .thenReturn(true);

            Ticket ticket = new Ticket(1, 1, 1, 2, false);
            when(mockTicketDAO.createTicket(1, 1, 2)).thenReturn(ticket);

            Ticket result = guestController.buyTicket(dto, "token");
            assertNotNull(result);
            assertEquals(2, result.getQuantity());
        }

        @Test
        void buyTicketShouldThrowNotFoundExceptionIfEventDoesNotExist() {
            BuyTicketDTO dto = new BuyTicketDTO();
            dto.setEventId(1);
            dto.setQuantity(2);
            dto.setPaymentMethod(PaymentMethod.CREDIT_CARD);

            when(mockEventDAO.getEventById(1)).thenReturn(null);

            assertThrows(NotFoundException.class, () -> guestController.buyTicket(dto, "token"));
        }

        @Test
        void buyTicketShouldThrowBadRequestExceptionIfNotEnoughTicketsAvailable() {
            BuyTicketDTO dto = new BuyTicketDTO();
            dto.setEventId(1);
            dto.setQuantity(10);
            dto.setPaymentMethod(PaymentMethod.CREDIT_CARD);

            Event event = new Event.Builder()
                .setId(1)
                
                .setTicketsAvailable(5)
                .build();
            when(mockEventDAO.getEventById(1)).thenReturn(event);

            assertThrows(BadRequestException.class, () -> guestController.buyTicket(dto, "token"));
        }
    }
}