package ru.practicum.shareit.booking.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
public class DateValidator implements ConstraintValidator<CheckDate, LocalDate[]> {
   /* private LocalDate dateOfStart;

    @Override
    public void initialize(CheckDate constraintAnnotation) {
        dateOfStart = LocalDate.parse(constraintAnnotation.value());
    }*/

    @Override
    public boolean isValid(LocalDate[] localDates, ConstraintValidatorContext constraintValidatorContext) {
        log.info("{}", localDates[0].isBefore(localDates[1]));
        return /*localDateTimes[0].isAfter(LocalDateTime.now()) && */ localDates[0].isBefore(localDates[1]);
    }

   /* @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        return localDate.isEqual(dateOfStart) || localDate.isAfter(dateOfStart);
    }*/

   /* @Override
    public boolean isValid(
            Object[] value,
            ConstraintValidatorContext context) {

        if (value[0] == null || value[1] == null) {
            return true;
        }

        if (!(value[0] instanceof LocalDate)
                || !(value[1] instanceof LocalDate)) {
            throw new IllegalArgumentException(
                    "Illegal method signature, expected two parameters of type LocalDate.");
        }

        return ((LocalDate) value[0]).isAfter(LocalDate.now())
                && ((LocalDate) value[0]).isBefore((LocalDate) value[1]);
    }

    @Override
    public boolean isValid(LocalDateTime localDateTime, ConstraintValidatorContext constraintValidatorContext) {
        return false;
    }*/
}
