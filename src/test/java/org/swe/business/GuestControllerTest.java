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
import org.swe.core.exceptions.ApplicationException;
import org.swe.core.exceptions.BadRequestException;
import org.swe.core.exceptions.NotFoundException;
import org.swe.core.exceptions.UnauthorizedException;
import org.swe.model.Event;
import org.swe.core.payment.PaymentContext;
import org.swe.core.payment.PaymentStrategy;
import org.swe.model.Ticket;
import org.swe.model.User;

class GuestControllerTest {

    private GuestController guestController;
    private AuthService mockAuthService;
    private EventDAO mockEventDAO;
    private TicketDAO mockTicketDAO;

    @BeforeEach
    void setUp() {
        // Mock dependencies
        VerifySessionService mockVerifySessionService = mock(VerifySessionService.class);
        UserDAO mockUserDAO = mock(UserDAO.class);
        mockAuthService = mock(AuthService.class);
        mockEventDAO = mock(EventDAO.class);
        mockTicketDAO = mock(TicketDAO.class);
      

        guestController = new GuestController(mockAuthService, mockVerifySessionService, mockEventDAO, mockTicketDAO,
                mockUserDAO);

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
            dto.setPaymentMethod("CREDIT_CARD");

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

            PaymentContext paymentContext;
            paymentContext = new PaymentContext();
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
            dto.setPaymentMethod("CREDIT_CARD");

            when(mockEventDAO.getEventById(1)).thenReturn(null);

            assertThrows(NotFoundException.class, () -> guestController.buyTicket(dto, "token"));
        }

        @Test
        void buyTicketShouldThrowBadRequestExceptionIfNotEnoughTicketsAvailable() {
            BuyTicketDTO dto = new BuyTicketDTO();
            dto.setEventId(1);
            dto.setQuantity(10); // <-- ASKING 10 TICKETS
            dto.setPaymentMethod("CREDIT_CARD");

            Event event = new Event.Builder()
                    .setId(1)
                    .setTitle("title")
                    .setDescription("description")
                    .setDate(Date.from(LocalDate.of(2025, 12, 12).atStartOfDay(ZoneId.systemDefault()).toInstant()))
                    .setTicketsAvailable(5) // <-- ONLY 5 TICKETS AVAILABLE
                    .build();
            when(mockEventDAO.getEventById(1)).thenReturn(event);

            assertThrows(BadRequestException.class, () -> guestController.buyTicket(dto, "token"));
        }

        @Test
        void buyTicketShouldThrowBadRequestExceptionIfPaymentFails() {
            BuyTicketDTO dto = new BuyTicketDTO();
            dto.setEventId(1);
            dto.setQuantity(2);
            dto.setPaymentMethod("CREDIT_CARD");

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
            when(paymentStrategy.processPayment(20)).thenReturn(false);

            assertThrows(BadRequestException.class, () -> guestController.buyTicket(dto, "token"));
        }

        @Test
        void buyTicketShouldThrowBadRequestExceptionIfEventUpdateFails() {
            BuyTicketDTO dto = new BuyTicketDTO();
            dto.setEventId(1);
            dto.setQuantity(2);
            dto.setPaymentMethod("CREDIT_CARD");

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

            when(mockEventDAO.updateEvent(
                    1,
                    event.getTitle(),
                    event.getDescription(),
                    event.getDate(),
                    3,
                    10)).thenReturn(false);

            assertThrows(BadRequestException.class, () -> guestController.buyTicket(dto, "token"));
        }



        @Test
        void buyTicketShouldThrowExceptionIfTokenValidationFails() {
            BuyTicketDTO dto = new BuyTicketDTO();
            dto.setEventId(1);
            dto.setQuantity(2);
            dto.setPaymentMethod("CREDIT_CARD");

            when(mockAuthService.validateAccessToken("token")).thenThrow(new UnauthorizedException("Invalid token"));

            assertThrows(UnauthorizedException.class, () -> guestController.buyTicket(dto, "token"));
        }

        @Test
        void buyTicketShouldThrowExceptionIfPaymentMethodIsInvalid() {
            BuyTicketDTO dto = new BuyTicketDTO();
            dto.setEventId(1);
            dto.setQuantity(2);
            dto.setPaymentMethod("invalid payment");

            assertThrows(ApplicationException.class, () -> guestController.buyTicket(dto, "token"));
        }
        

        @Test
        void buyTicketShouldThrowExceptionIfPaymentGoesWrong(){
            BuyTicketDTO dto = new BuyTicketDTO();
            dto.setEventId(1);
            dto.setQuantity(2);
            dto.setPaymentMethod("CREDIT_CARD");

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
            when(paymentStrategy.processPayment(20)).thenReturn(false);

            assertThrows(BadRequestException.class, () -> guestController.buyTicket(dto, "token"));
        }
    }
}