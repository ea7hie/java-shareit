package ru.practicum.shareit.request.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ItemRequestDtoForCreateTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenDescriptionIsNull_thenValidationFails() {
        ItemRequestDtoForCreate dto = new ItemRequestDtoForCreate();
        dto.setDescription(null);

        Set<ConstraintViolation<ItemRequestDtoForCreate>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description")));
    }

    @Test
    void whenDescriptionIsBlank_thenValidationFails() {
        ItemRequestDtoForCreate dto = new ItemRequestDtoForCreate();
        dto.setDescription("   ");

        Set<ConstraintViolation<ItemRequestDtoForCreate>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description")));
    }

    @Test
    void whenDescriptionIsValid_thenValidationPasses() {
        ItemRequestDtoForCreate dto = new ItemRequestDtoForCreate();
        dto.setDescription("Some valid description");

        Set<ConstraintViolation<ItemRequestDtoForCreate>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }
}
