package org.swe.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class TicketTest {

    private Ticket createValidTicket() {
        return new Ticket(1, 101, 2, 1, false);
    }

    @Test
    public void testTicketConstructor_InvalidQuantity() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Ticket(1, 101, 2, 0, false);
        });

        assertEquals("Quantity must be at least 1.", exception.getMessage());
    }

    @Test
    public void testSetters_ValidData() {
        Ticket ticket = createValidTicket();

        ticket.setId(2);
        ticket.setUserId(102);
        ticket.setEventId(3);
        ticket.setQuantity(5);
        ticket.setUsed(true);

        assertEquals(2, ticket.getId(), "Ticket ID should be updated to 2");
        assertEquals(102, ticket.getUserId(), "User ID should be updated to 102");
        assertEquals(3, ticket.getEventId(), "Event ID should be updated to 3");
        assertEquals(5, ticket.getQuantity(), "Quantity should be updated to 5");
        assertTrue(ticket.isUsed(), "Used flag should be updated to true");
    }

    @Test
    public void testSetters_InvalidId() {
        Ticket ticket = createValidTicket();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ticket.setId(0);
        });

        assertEquals("Ticket ID must be greater than 0.", exception.getMessage());
    }

    @Test
    public void testSetters_InvalidUserId() {
        Ticket ticket = createValidTicket();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ticket.setUserId(0);
        });

        assertEquals("User ID must be greater than 0.", exception.getMessage());
    }

    @Test
    public void testSetters_InvalidEventId() {
        Ticket ticket = createValidTicket();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ticket.setEventId(0);
        });

        assertEquals("Event ID must be greater than 0.", exception.getMessage());
    }

    @Test
    public void testSetters_InvalidQuantity() {
        Ticket ticket = createValidTicket();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ticket.setQuantity(0);
        });

        assertEquals("Quantity must be at least 1.", exception.getMessage());
    }

    @Test
    public void testSetters_ValidUsedFlag() {
        Ticket ticket = createValidTicket();

        ticket.setUsed(true);
        assertTrue(ticket.isUsed(), "Used flag should be updated to true");

        ticket.setUsed(false);
        assertFalse(ticket.isUsed(), "Used flag should be updated to false");
    }

}