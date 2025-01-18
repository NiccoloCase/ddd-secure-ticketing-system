package org.swe.model;

import java.util.Date;

public class Ticket {
    private int id;
    private int userId;
    private int eventId;
    private int quantity;
    private boolean used;


    public Ticket(int id, int userId, int eventId, int quantity, boolean used) {
        this.id = id;
        this.userId = userId;
        this.quantity = quantity;
        this.used = used;
        this.eventId = eventId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }
}
