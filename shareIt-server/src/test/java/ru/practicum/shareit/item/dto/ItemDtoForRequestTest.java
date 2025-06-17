package ru.practicum.shareit.item.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ItemDtoForRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenNameIsBlank_thenValidationFails() {
        ItemDtoForRequest dto = new ItemDtoForRequest();
        dto.setId(1L);
        dto.setName(""); // пустая строка - невалидно
        dto.setOwnerId(100L);

        Set<ConstraintViolation<ItemDtoForRequest>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty(), "Expected validation violations for blank name");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")),
                "Violation should be on 'name' field");
    }

    @Test
    void whenNameIsNull_thenValidationFails() {
        ItemDtoForRequest dto = new ItemDtoForRequest();
        dto.setId(2L);
        dto.setName(null); // null - невалидно
        dto.setOwnerId(100L);

        Set<ConstraintViolation<ItemDtoForRequest>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty(), "Expected validation violations for null name");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")),
                "Violation should be on 'name' field");
    }

    @Test
    void whenNameIsValid_thenValidationPasses() {
        ItemDtoForRequest dto = new ItemDtoForRequest();
        dto.setId(3L);
        dto.setName("Valid Item Name");
        dto.setOwnerId(100L);

        Set<ConstraintViolation<ItemDtoForRequest>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty(), "Expected no validation violations for valid name");
    }
}