package org.swe.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;

public class EventTest {

    @Test
    public void testEventConstructorAndGetters() {
        Date eventDate = new Date();
        Event event = new Event(1, "Concert", "Music concert", eventDate, 100, 50.0);

        assertEquals(1, event.getId());
        assertEquals("Concert", event.getTitle());
        assertEquals("Music concert", event.getDescription());
        assertEquals(eventDate, event.getDate());
        assertEquals(100, event.getTicketsAvailable());
        assertEquals(50.0, event.getTicketPrice(), 0.01);
    }

    @Test
    public void testSetters() {
        Event event = new Event(0, "", "", new Date(), 0, 0.0);

        event.setId(2);
        event.setTitle("Theater Play");
        event.setDescription("Drama performance");
        event.setDate(new Date(1700000000000L)); // some specific timestamp
        event.setTicketsAvailable(200);
        event.setTicketPrice(75.0);

        assertEquals(2, event.getId());
        assertEquals("Theater Play", event.getTitle());
        assertEquals("Drama performance", event.getDescription());
        assertEquals(new Date(1700000000000L), event.getDate());
        assertEquals(200, event.getTicketsAvailable());
        assertEquals(75.0, event.getTicketPrice(), 0.01);
    }

    @Test
    public void testTicketPriceValidation() {
        Event event = new Event(1, "Festival", "Outdoor festival", new Date(), 150, 25.0);
        assertTrue(event.getTicketPrice() >= 0, "Ticket price should be positive or zero.");
    }

    @Test
    public void testTicketsAvailableValidation() {
        Event event = new Event(1, "Exhibition", "Art exhibition", new Date(), 50, 10.0);
        assertTrue(event.getTicketsAvailable() >= 0, "Tickets available should be non-negative.");
    }
}
