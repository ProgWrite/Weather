package org.example.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



@Constraint(validatedBy = ExistingLoginValidator.class )
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistingLogin {

    String message() default "User with this login doesn't exist. Please try to register with this login";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {

    };

}
