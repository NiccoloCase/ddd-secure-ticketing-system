package org.swe.model;


public class VerifySession {

    private User user;
    private Integer staffId;
    private Integer eventId;
    private VerifySessionStatus status;


    public VerifySession(Integer staffId, Integer eventId) {
        this.staffId = staffId;
        this.eventId = eventId;
        this.status = VerifySessionStatus.WAITING_FOR_GUEST;
    }

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

    public void linkSessionToUser(User user) {
        this.user = user;
        this.status = VerifySessionStatus.PENDING;
    }

    public User getUser() {
        return user; 
    }

    public VerifySessionStatus getStatus() {
        return status;
    }

    public void setStatus(VerifySessionStatus status) {
        this.status = status;
    }


}
