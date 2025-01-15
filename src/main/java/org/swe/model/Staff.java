package org.swe.model;

public class Staff  extends User {

    private int eventId;

    public Staff(String name, String surname, String passwordHash, String email, int eventId) {
        super(name, surname, passwordHash, email);
        this.eventId = eventId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }
}
