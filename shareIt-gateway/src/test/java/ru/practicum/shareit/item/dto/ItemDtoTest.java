package ru.practicum.shareit.item.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ItemDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private ItemDto createValidItemDto() {
        return new ItemDto(
                1L,
                "Drill",
                "Cordless electric drill",
                10L,
                0L,
                true,
                List.of() // можно добавить комментарии при необходимости
        );
    }

    @Test
    void shouldPassValidationWhenNameAndDescriptionAreNotBlank() {
        ItemDto dto = createValidItemDto();
        Set<ConstraintViolation<ItemDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Validation should pass with valid name and description");
    }

    @Test
    void shouldFailValidationWhenNameIsBlank() {
        ItemDto dto = createValidItemDto();
        dto.setName("  ");

        Set<ConstraintViolation<ItemDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty(), "Validation should fail when name is blank");
    }

    @Test
    void shouldFailValidationWhenDescriptionIsBlank() {
        ItemDto dto = createValidItemDto();
        dto.setDescription(" ");

        Set<ConstraintViolation<ItemDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty(), "Validation should fail when description is blank");
    }

    @Test
    void shouldFailValidationWhenNameIsNull() {
        ItemDto dto = createValidItemDto();
        dto.setName(null);

        Set<ConstraintViolation<ItemDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty(), "Validation should fail when name is null");
    }

    @Test
    void shouldFailValidationWhenDescriptionIsNull() {
        ItemDto dto = createValidItemDto();
        dto.setDescription(null);

        Set<ConstraintViolation<ItemDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty(), "Validation should fail when description is null");
    }
}