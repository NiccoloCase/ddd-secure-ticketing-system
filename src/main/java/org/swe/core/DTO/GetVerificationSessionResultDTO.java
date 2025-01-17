package org.swe.core.DTO;

import jakarta.validation.constraints.NotNull;

public class GetVerificationSessionResultDTO {

    @NotNull
    private Integer sessionId;

}
