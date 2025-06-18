package ru.practicum.shareit.booking.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BookingDtoPostTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldPassValidationWithFutureDates() {
        BookingDtoPost dto = new BookingDtoPost(
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                1L
        );

        Set<ConstraintViolation<BookingDtoPost>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Должно быть без ошибок валидации");
    }

    @Test
    void shouldFailValidationWithPastStartDate() {
        BookingDtoPost dto = new BookingDtoPost(
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1),
                1L
        );

        Set<ConstraintViolation<BookingDtoPost>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty(), "Должна быть ошибка валидации из-за start в прошлом");
    }

    @Test
    void shouldFailValidationWithPastEndDate() {
        BookingDtoPost dto = new BookingDtoPost(
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().minusDays(1),
                1L
        );

        Set<ConstraintViolation<BookingDtoPost>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty(), "Должна быть ошибка валидации из-за end в прошлом");
    }

    @Test
    void shouldFailValidationWithBothDatesInPast() {
        BookingDtoPost dto = new BookingDtoPost(
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1),
                1L
        );

        Set<ConstraintViolation<BookingDtoPost>> violations = validator.validate(dto);
        assertEquals(2, violations.size(), "Ожидаются ошибки валидации для start и end");
    }
}