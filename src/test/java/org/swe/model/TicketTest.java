package org.swe.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TicketTest {

    @Test
    public void testTicketConstructorAndGetters() {
        Ticket ticket = new Ticket(1, 101, 2, 12345, false);

        assertEquals(1, ticket.getId());
        assertEquals(101, ticket.getUserId());
        assertEquals(2, ticket.getQuantity());
        assertEquals(12345, ticket.getCode());
        assertFalse(ticket.isUsed());
    }

    @Test
    public void testSetters() {
        Ticket ticket = new Ticket(0, 0, 0, 0, false);

        ticket.setId(2);
        ticket.setUserId(102);
        ticket.setQuantity(3);
        ticket.setCode(54321);
        ticket.setUsed(true);

        assertEquals(2, ticket.getId());
        assertEquals(102, ticket.getUserId());
        assertEquals(3, ticket.getQuantity());
        assertEquals(54321, ticket.getCode());
        assertTrue(ticket.isUsed());
    }
}
