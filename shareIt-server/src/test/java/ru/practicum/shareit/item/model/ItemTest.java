package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.*;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldCreateItemCorrectly() {
        Item item = new Item(1L, "Drill", "Powerful drill", 2L, 3L, true);

        assertThat(item.getId()).isEqualTo(1L);
        assertThat(item.getName()).isEqualTo("Drill");
        assertThat(item.getDescription()).isEqualTo("Powerful drill");
        assertThat(item.getOwnerId()).isEqualTo(2L);
        assertThat(item.getRequestId()).isEqualTo(3L);
        assertThat(item.isAvailable()).isTrue();
    }

    @Test
    void shouldDetectBlankName() {
        Item item = new Item(null, " ", "Some description", 1L, null, true);
        Set<ConstraintViolation<Item>> violations = validator.validate(item);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }

    @Test
    void shouldDetectBlankDescription() {
        Item item = new Item(null, "Hammer", "", 1L, null, true);
        Set<ConstraintViolation<Item>> violations = validator.validate(item);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("description"));
    }

    @Test
    void shouldPassValidationWithValidFields() {
        Item item = new Item(null, "Saw", "Sharp hand saw", 1L, 2L, false);
        Set<ConstraintViolation<Item>> violations = validator.validate(item);

        assertThat(violations).isEmpty();
    }
}