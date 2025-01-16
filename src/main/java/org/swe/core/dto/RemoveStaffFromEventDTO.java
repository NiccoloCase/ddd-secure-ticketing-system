package org.swe.core.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public class RemoveStaffFromEventDTO {

    @NotNull(message = "Event ID cannot be null.")
    private Integer eventId;

    @NotNull(message = "Staff email cannot be null.")
    @Email(message = "Staff email must be a valid email address.")
    private String staffEmail;

    public RemoveStaffFromEventDTO(Integer eventId, String staffEmail) {
        this.eventId = eventId;
        this.staffEmail = staffEmail;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public String getStaffEmail() {
        return staffEmail;
    }

    public void setStaffEmail(String staffEmail) {
        this.staffEmail = staffEmail;
    }
}
