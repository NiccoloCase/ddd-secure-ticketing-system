package org.swe.model;

public final class Ticket {
    private int id;
    private int userId;
    private int eventId;
    private int quantity;
    private boolean used;


    public Ticket(int id, int userId, int eventId, int quantity, boolean used) {
        this.setId(id);
        this.setUserId(userId);
        this.setEventId(eventId);
        this.setQuantity(quantity);
        this.setUsed(used);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Ticket ID must be greater than 0.");
        }
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID must be greater than 0.");
        }
        this.userId = userId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        if (eventId <= 0) {
            throw new IllegalArgumentException("Event ID must be greater than 0.");
        }
        this.eventId = eventId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1.");
        }
        this.quantity = quantity;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", userId=" + userId +
                ", eventId=" + eventId +
                ", quantity=" + quantity +
                ", used=" + used +
                '}';
    }

}