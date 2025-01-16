package org.swe.core.dto;

import jakarta.validation.constraints.NotNull;

public class DeleteEventDTO {

    @NotNull(message = "Event ID cannot be null.")
    private Integer eventId;

    public DeleteEventDTO(Integer eventId) {
        this.eventId = eventId;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }
}
