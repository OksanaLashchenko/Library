package com.example.demo.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * EmailConstraint is a custom annotation, designed for validation management
 * inside the Library Application.
 * It can be applied above methods and fields containing or returning contact emails
 * of users, which must be checked on their validity upon creation or coming into the
 * application.
 */
@Documented
@Constraint(validatedBy = EmailValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailConstraint {

    /**
     * @return default message for invalid entities
     */
    String message() default "Invalid email";

    /**
     * @return - used to meet some Spring standards.
     */
    Class<?>[] groups() default {};

    /**
     * @return - used to assign custom payload objects to a constraint.
     *      * This attribute is not used by the API itself.
     */
    Class<? extends Payload>[] payload() default {};
}
