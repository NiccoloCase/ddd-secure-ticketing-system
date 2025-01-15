package org.swe.model;

public class Admin  extends User {

    private int eventId;

    public Admin(String name, String surname, String passwordHash, String email, int id, int eventId) {
        super(name, surname, passwordHash, email, id);
        this.eventId = eventId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }
}
