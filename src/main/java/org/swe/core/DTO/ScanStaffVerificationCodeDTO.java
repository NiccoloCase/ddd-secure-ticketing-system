package org.swe.core.DTO;

import jakarta.validation.constraints.NotNull;

public class ScanStaffVerificationCodeDTO {

    @NotNull
    private String code;

    public String getCode() {
        return code;
    }

}
