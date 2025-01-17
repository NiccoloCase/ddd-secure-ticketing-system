package org.swe.model;

public class VerifySession {
    private Integer staffId;
    private Integer eventId;
    private Integer ticketId;
    private VerifySessionStatus status;


    public VerifySession(Integer staffId, Integer eventId, Integer ticketId) {
        this.staffId = staffId;
        this.eventId = eventId;
        this.ticketId = ticketId;
        this.status = VerifySessionStatus.PENDING;
    }

    // Getters, setters
    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public Integer getTicketId() {
        return ticketId;
    }

    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }

    public VerifySessionStatus getStatus() {
        return status;
    }

    public void setStatus(VerifySessionStatus status) {
        this.status = status;
    }


}
