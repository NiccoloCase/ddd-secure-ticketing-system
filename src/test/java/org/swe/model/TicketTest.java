package org.swe.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class TicketTest {

    @Test
    public void testTicketConstructorAndGetters() {
        Ticket ticket = new Ticket(1, 101, 2,1, false);

        assertEquals(1, ticket.getId());
        assertEquals(101, ticket.getUserId());
        assertEquals(1, ticket.getQuantity());
        assertEquals(2, ticket.getEventId());
        assertFalse(ticket.isUsed());
    }

    @Test
    public void testSetters() {
        Ticket ticket = new Ticket(0, 0, 0,0, false);

        ticket.setId(2);
        ticket.setUserId(102);
        ticket.setQuantity(3);
        ticket.setUsed(true);

        assertEquals(2, ticket.getId());
        assertEquals(102, ticket.getUserId());
        assertEquals(3, ticket.getQuantity());
        assertTrue(ticket.isUsed());
    }
}
