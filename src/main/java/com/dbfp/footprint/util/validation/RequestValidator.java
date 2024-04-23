package com.dbfp.footprint.util.validation;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


import java.beans.BeanProperty;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class RequestValidator {

    private final Validator validator;

    public RequestValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    public void validate(Object target, Class<?>... groups) {
        Set<ConstraintViolation<Object>> violations = validator.validate(target, groups);

        if (!violations.isEmpty()) {
            throw new CustomValidationException((Set<ConstraintViolation<?>>) (Set<?>) violations);
        }

    }
}

