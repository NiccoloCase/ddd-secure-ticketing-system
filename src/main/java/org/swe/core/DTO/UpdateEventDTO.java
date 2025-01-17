package org.swe.core.DTO;

import jakarta.validation.constraints.*;

import java.util.Date;

public class UpdateEventDTO {

    @NotNull(message = "Event ID cannot be null.")
    private Integer eventId;

    @NotBlank(message = "Title cannot be empty.")
    private String title;

    @NotBlank(message = "Description cannot be empty.")
    private String description;

    @Future(message = "Date must be in the future.")
    private Date date;

    @Min(value = 0, message = "Tickets available must be 0 or greater.")
    private int ticketsAvailable;

    @Positive(message = "Ticket price must be greater than 0.")
    private double ticketPrice;

    public UpdateEventDTO(Integer eventId, String title, String description, Date date, int ticketsAvailable, double ticketPrice) {
        this.eventId = eventId;
        this.title = title;
        this.description = description;
        this.date = date;
        this.ticketsAvailable = ticketsAvailable;
        this.ticketPrice = ticketPrice;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getTicketsAvailable() {
        return ticketsAvailable;
    }

    public void setTicketsAvailable(int ticketsAvailable) {
        this.ticketsAvailable = ticketsAvailable;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }
}
