package org.swe.core.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginDTO {

    @NotBlank(message = "Email cannot be empty.")
    @Email(message = "Email must be a valid email address.")
    public String email;

    @NotBlank(message = "Password cannot be empty.")
    public String password;

    public LoginDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    

}
