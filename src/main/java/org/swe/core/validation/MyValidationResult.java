package org.swe.core.validation;

import jakarta.validation.ConstraintViolation;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class MyValidationResult<T> {
    private final Set<ValidationError> errors;

    public MyValidationResult(Set<ConstraintViolation<T>> violations) {
        this.errors = violations.stream()
                .map(violation -> new ValidationError(
                        violation.getPropertyPath().toString(),
                        violation.getMessage(),
                        violation.getInvalidValue()
                ))
                .collect(Collectors.toSet());
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public Set<ValidationError> getErrors() {
        return new HashSet<>(errors);
    }

    public Set<ValidationError> getErrorsForField(String fieldName) {
        return errors.stream()
                .filter(error -> error.field().equals(fieldName))
                .collect(Collectors.toSet());
    }

    public String getErrorMessage() {
        return errors.stream()
                .map(error -> error.field() + ": " + error.message())
                .collect(Collectors.joining(", "));
    }



    public record ValidationError(
            String field,
            String message,
            Object invalidValue
    ) {}
}