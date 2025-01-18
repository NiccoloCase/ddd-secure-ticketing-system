package org.swe.core.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateUserDTO {
    @NotBlank(message = "Name cannot be empty.")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 100 characters.")
    private String name;

    @NotBlank(message = "Username cannot be empty.")
    @Size(min = 2, max = 50, message = "Username must be between 2 and 100 characters.")
    private String surname;

    @NotBlank(message = "Email cannot be empty.")
    @Email(message = "Email must be a valid email address.")
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @NotBlank(message = "Password cannot be empty.")
    private String password;


    public CreateUserDTO(String name,String surname, String email, String password) {
        this.name = name;
        this.email = email;
        this.surname = surname;
        this.password = password;
    }


}
