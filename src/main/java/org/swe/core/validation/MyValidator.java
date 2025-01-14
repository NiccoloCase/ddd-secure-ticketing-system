package org.swe.core.validation;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import java.util.Set;

public class MyValidator<T> {
    private final Validator validator;

    public MyValidator() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            this.validator = factory.getValidator();
        }
    }

    public MyValidationResult<T> validate(T dto) {
        Set<ConstraintViolation<T>> violations = validator.validate(dto);
        return new MyValidationResult<>(violations);
    }
}