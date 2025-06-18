package ru.practicum.shareit.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ItemRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllFieldsValid_thenNoViolations() {
        ItemRequest itemRequest = new ItemRequest(
                1L,
                "Description",
                10L,
                LocalDateTime.now()
        );

        Set<ConstraintViolation<ItemRequest>> violations = validator.validate(itemRequest);
        assertThat(violations).isEmpty();
    }

    @Test
    void whenDescriptionIsBlank_thenViolation() {
        ItemRequest itemRequest = new ItemRequest(
                1L,
                "   ",
                10L,
                LocalDateTime.now()
        );

        Set<ConstraintViolation<ItemRequest>> violations = validator.validate(itemRequest);
        assertThat(violations).isNotEmpty();

        boolean hasDescriptionViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("description"));
        assertThat(hasDescriptionViolation).isTrue();
    }

    @Test
    void whenCreatedIsNull_thenViolation() {
        ItemRequest itemRequest = new ItemRequest(
                1L,
                "Valid description",
                10L,
                null
        );

        Set<ConstraintViolation<ItemRequest>> violations = validator.validate(itemRequest);
        assertThat(violations).isNotEmpty();

        boolean hasCreatedViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("created"));
        assertThat(hasCreatedViolation).isTrue();
    }
}