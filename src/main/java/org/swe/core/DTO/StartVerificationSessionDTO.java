package org.swe.core.DTO;

import jakarta.validation.constraints.NotNull;

public class StartVerificationSessionDTO {
    @NotNull(message = "Event ID cannot be null.")
    private Integer eventId;

    public StartVerificationSessionDTO(Integer eventId) {
        this.eventId = eventId;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }
}
