package org.swe.core.DTO;

import jakarta.validation.constraints.NotNull;

public class ValidateVerificationSessionDTO {

    @NotNull
    private String sessionKey;

    public ValidateVerificationSessionDTO(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }
}
