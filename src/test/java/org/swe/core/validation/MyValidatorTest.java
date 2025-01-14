package org.swe.core.validation;
import static org.junit.jupiter.api.Assertions.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.junit.jupiter.api.Test;

class MyValidatorTest {

    private static class TestDTO {
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

        public TestDTO(String name, String surname, String email, String password) {
            this.name = name;
            this.surname = surname;
            this.email = email;
            this.password = password;
        }
    }

    @Test
    void testValidDTO() {
        TestDTO dto = new TestDTO("John", "Doe", "jon@gmial.com", "password");
        MyValidator<TestDTO> validator = new MyValidator<>();
        MyValidationResult<TestDTO> x = validator.validate(dto);
        assertFalse(x.hasErrors(), "DTO should be valid");
    }

    @Test
    void testEmptyDTO() {
        TestDTO dto = new TestDTO("", "", "", "");
        MyValidator<TestDTO> validator = new MyValidator<>();
        MyValidationResult<TestDTO> x = validator.validate(dto);
        assertTrue(x.hasErrors(), "DTO should be invalid");
        assertTrue(x.getErrors().size() > 4, "At least 4 errors should be present");
    }

    @Test
    void testInvalidDTO(){
        TestDTO dto = new TestDTO("J", "D", "jon@gmial", "pass");
        MyValidator<TestDTO> validator = new MyValidator<>();
        MyValidationResult<TestDTO> x = validator.validate(dto);
        assertTrue(x.hasErrors(), "DTO should be invalid");
    }
}