package org.swe.core.DTO;

import jakarta.validation.constraints.NotNull;

public class GetVerificationSessionResultDTO {

    @NotNull
    private String sessionKey;

    public GetVerificationSessionResultDTO(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }
}
