package org.swe;
import org.swe.core.dto.CreateUserDTO;
import org.swe.core.validation.MyValidationResult;
import org.swe.core.validation.MyValidator;
import org.swe.helpers.Config;

public class Main {
    public static void main(String[] args) {


        Config.init();

        System.out.println("Hello world!");


        MyValidator<CreateUserDTO> validator = new MyValidator<>();
        CreateUserDTO user = new CreateUserDTO("A", "B", "invalid-email", "password");
        MyValidationResult<CreateUserDTO> result = validator.validate(user);


        if (result.hasErrors()) {

            for (MyValidationResult.ValidationError error : result.getErrors()) {
                System.out.println("Campo: " + error.field());
                System.out.println("Messaggio: " + error.message());
                System.out.println("Valore non valido: " + error.invalidValue());
            }
        }


    }
}