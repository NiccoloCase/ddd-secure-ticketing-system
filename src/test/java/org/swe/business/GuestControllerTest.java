package org.swe.business;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.swe.core.DAO.EventDAO;
import org.swe.core.DAO.TicketDAO;
import org.swe.core.DAO.UserDAO;
import org.swe.core.DTO.BuyTicketDTO;
import org.swe.core.exceptions.BadRequestException;
import org.swe.core.exceptions.NotFoundException;
import org.swe.model.Event;
import org.swe.model.PaymentMethod;
import org.swe.model.Ticket;

class GuestControllerTest {

    private VerifySessionService verifySessionServiceMock;
    private EventDAO eventDAOMock;
    private TicketDAO ticketDAOMock;
    private UserDAO userDAOMock;
    private AuthService authServiceMock;
    private GuestController guestController;

    private Event mockEvent;

    @BeforeEach
    void setUp() {

        verifySessionServiceMock = mock(VerifySessionService.class);
        eventDAOMock = mock(EventDAO.class);
        ticketDAOMock = mock(TicketDAO.class);
        userDAOMock = mock(UserDAO.class);
        authServiceMock = mock(AuthService.class);

        guestController = new GuestController(
                authServiceMock,
                verifySessionServiceMock,
                eventDAOMock,
                ticketDAOMock,
                userDAOMock);

        mockEvent = new Event.Builder()
                .setId(100)
                .setTitle("Concerto")
                .setDescription("Concerto di prova")
                .setDate(new Date(System.currentTimeMillis() + 86400000)) // domani
                .setTicketsAvailable(10)
                .setTicketPrice(50.0)
                .build();

    }

    @Test
    void buyTicket_Successful() {

        BuyTicketDTO dto = new BuyTicketDTO();
        dto.setEventId(100);
        dto.setQuantity(2);
        dto.setPaymentMethod(PaymentMethod.CREDIT_CARD);

        when(eventDAOMock.getEventById(100)).thenReturn(mockEvent);

        when(eventDAOMock.updateEvent(
                eq(100),
                anyString(),
                anyString(),
                any(Date.class),
                eq(8), 
                eq(50.0))).thenReturn(true);

        Ticket createdTicket = new Ticket(1, 1, 100, 2, false);
        when(ticketDAOMock.createTicket(1, 100, 2)).thenReturn(createdTicket);

        Ticket result = guestController.buyTicket(dto, "fakeToken");

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(1, result.getUserId());
        assertEquals(100, result.getEventId());
        assertEquals(2, result.getQuantity());
        verify(eventDAOMock).getEventById(100);
        verify(ticketDAOMock).createTicket(1, 100, 2);
    }

    @Test
    void buyTicket_EventDoesNotExist() {
        BuyTicketDTO dto = new BuyTicketDTO();
        dto.setEventId(999);
        dto.setQuantity(2);
        dto.setPaymentMethod(PaymentMethod.CREDIT_CARD);


        assertThrows(NotFoundException.class, () -> guestController.buyTicket(dto, "fakeToken"));
    }

    @Test
    void buyTicket_TicketsNotEnough() {
        BuyTicketDTO dto = new BuyTicketDTO();
        dto.setEventId(100);
        dto.setQuantity(20);
        dto.setPaymentMethod(PaymentMethod.CREDIT_CARD);

        // L'evento esiste ma ha solo 10 ticket
        when(eventDAOMock.getEventById(100)).thenReturn(mockEvent);

        assertThrows(BadRequestException.class, () -> guestController.buyTicket(dto, "fakeToken"));
    }

    @Test
    void buyTicket_PaymentMethodIsUnknown() {
        BuyTicketDTO dto = new BuyTicketDTO();
        dto.setEventId(100);
        dto.setQuantity(2);
        dto.setPaymentMethod(PaymentMethod.UNKNOWN); 

        when(eventDAOMock.getEventById(100)).thenReturn(mockEvent);

        assertThrows(BadRequestException.class, () -> guestController.buyTicket(dto, "fakeToken"));
    }

}