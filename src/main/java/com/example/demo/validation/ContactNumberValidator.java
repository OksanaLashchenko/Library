package com.example.demo.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ContactNumberValidator implements ConstraintValidator<ContactNumberConstraint, String> {
    private static final String CONTACT_NUMBER_PATTERN = "[0-9]+";

    @Override
    public void initialize(ContactNumberConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String contactNumber, ConstraintValidatorContext context) {
        return contactNumber != null && contactNumber.matches(CONTACT_NUMBER_PATTERN)
                && (contactNumber.length() > 8) && (contactNumber.length() < 14);
    }
}
