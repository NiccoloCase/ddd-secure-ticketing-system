package org.swe.core.dto;

import jakarta.validation.constraints.NotNull;

public class GetValidationSessionResultDTO {

    @NotNull
    private Integer sessionId;

}
