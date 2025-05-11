package ru.practicum.shareit.booking.validations;

import jakarta.validation.Constraint;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = DateValidator.class)
public @interface CheckDate {
    String message() default "Дата окончания аренды не может быть раньше начала.";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};

   // String value() default "1895-12-28";
}