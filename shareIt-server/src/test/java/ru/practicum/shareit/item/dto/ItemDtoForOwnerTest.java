package ru.practicum.shareit.item.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ItemDtoForOwnerTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenNameIsBlank_thenValidationFails() {
        ItemDtoForOwner dto = new ItemDtoForOwner();
        dto.setId(1);
        dto.setName("  ");  // пустое имя
        dto.setDescription("Valid description");
        dto.setAvailable(true);

        Set<ConstraintViolation<ItemDtoForOwner>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());

        boolean hasNotBlankViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name")
                        && v.getMessage().contains("не должно быть пустым"));
        assertTrue(hasNotBlankViolation);
    }

    @Test
    void whenDescriptionIsBlank_thenValidationFails() {
        ItemDtoForOwner dto = new ItemDtoForOwner();
        dto.setId(1);
        dto.setName("Valid name");
        dto.setDescription("");
        dto.setAvailable(true);

        Set<ConstraintViolation<ItemDtoForOwner>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());

        boolean hasNotBlankViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("description")
                        && v.getMessage().contains("не должно быть пустым"));
        assertTrue(hasNotBlankViolation);
    }

    @Test
    void whenValidDto_thenNoValidationErrors() {
        ItemDtoForOwner dto = new ItemDtoForOwner(
                1L,
                "Item name",
                "Item description",
                true,
                null,
                null,
                Collections.emptyList()
        );

        Set<ConstraintViolation<ItemDtoForOwner>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }
}