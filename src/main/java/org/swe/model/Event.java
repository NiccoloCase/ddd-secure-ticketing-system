package org.swe.model;

import java.util.Date;

public class Event {
    private int id;
    private String title;
    private String description;
    private Date date;
    private int ticketsAvailable;
    private double ticketPrice;


    public Event(int id, String title, String description, Date date, int ticketsAvailable, double ticketPrice) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.ticketsAvailable = ticketsAvailable;
        this.ticketPrice = ticketPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setTicketPrice(double ticketsPrice) {
        this.ticketPrice = ticketsPrice;
    }
}
