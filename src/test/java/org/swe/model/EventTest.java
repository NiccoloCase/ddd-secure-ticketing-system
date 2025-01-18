package org.swe.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;

public class EventTest {

    @Test
    public void testEventBuilderAndGetters() {
        Date eventDate = new Date(System.currentTimeMillis() + 86400000); // Tomorrow
        Event event = new Event.Builder()
                .setId(1)
                .setTitle("Concert")
                .setDescription("Music concert")
                .setDate(eventDate)
                .setTicketsAvailable(100)
                .setTicketPrice(50.0)
                .build();

        assertEquals(1, event.getId());
        assertEquals("Concert", event.getTitle());
        assertEquals("Music concert", event.getDescription());
        assertEquals(eventDate, event.getDate());
        assertEquals(100, event.getTicketsAvailable());
        assertEquals(50.0, event.getTicketPrice(), 0.01);
    }

    @Test
    public void testInvalidIdThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Event.Builder().setId(0); // ID not valid
        });
        assertEquals("ID must be greater than 0.", exception.getMessage());
    }

    @Test
    public void testInvalidTitleThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Event.Builder().setTitle(""); // Title not valid
        });
        assertEquals("Title cannot be null or empty.", exception.getMessage());
    }

    @Test
    public void testInvalidDateThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Event.Builder().setDate(new Date(System.currentTimeMillis() - 86400000)); // Past date
        });
        assertEquals("Date must not be null and must be in the future.", exception.getMessage());
    }

    @Test
    public void testNegativeTicketsAvailableThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Event.Builder().setTicketsAvailable(-1); // Negative tickets
        });
        assertEquals("Tickets available must be 0 or greater.", exception.getMessage());
    }

    @Test
    public void testNegativeTicketPriceThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Event.Builder().setTicketPrice(-10.0); // Negative price
        });
        assertEquals("Ticket price must be 0 or greater.", exception.getMessage());
    }

    @Test
    public void testBuildWithoutTitleThrowsException() {
        Exception exception = assertThrows(NullPointerException.class, () -> new Event.Builder()
                .setId(1)
                .setDescription("Some description")
                .setDate(new Date(System.currentTimeMillis() + 86400000))
                .setTicketsAvailable(10)
                .setTicketPrice(20.0)
                .build());
        assertEquals("Title must not be null.", exception.getMessage());
    }
}
