package org.swe.core.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateUserDTO {
    @NotBlank(message = "Name cannot be empty.")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 100 characters.")
    public String name;

    @NotBlank(message = "Username cannot be empty.")
    @Size(min = 2, max = 50, message = "Username must be between 2 and 100 characters.")
    public String surname;

    @NotBlank(message = "Email cannot be empty.")
    @Email(message = "Email must be a valid email address.")
    public String email;

    @NotBlank(message = "Password cannot be empty.")
    public String password;


    public CreateUserDTO(String name,String surname, String email, String password) {
        this.name = name;
        this.email = email;
        this.surname = surname;
        this.password = password;
    }
}
