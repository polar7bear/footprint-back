package com.dbfp.footprint.util.validation;

import jakarta.validation.ConstraintViolation;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomValidationException extends RuntimeException {

    private Set<ConstraintViolation<?>> constraintViolations;

    public CustomValidationException(Set<ConstraintViolation<?>> constraintViolations) {
        super("Validation failed: " + buildMessage(constraintViolations));
        this.constraintViolations = constraintViolations;
    }

    private static String buildMessage(Set<ConstraintViolation<?>> constraintViolations) {
        return constraintViolations.stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));
    }

    public Set<ConstraintViolation<?>> getConstraintViolations() {
        return constraintViolations;
    }
}
