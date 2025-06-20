package ru.practicum.shareit.request.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

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

        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("description");
    }

    @Test
    void whenDescriptionIsBlank_thenValidationFails() {
        ItemRequestDtoForCreate dto = new ItemRequestDtoForCreate();
        dto.setDescription("   "); // пробельная строка

        Set<ConstraintViolation<ItemRequestDtoForCreate>> violations = validator.validate(dto);

        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("description");
    }

    @Test
    void whenDescriptionIsValid_thenValidationPasses() {
        ItemRequestDtoForCreate dto = new ItemRequestDtoForCreate();
        dto.setDescription("Valid description");

        Set<ConstraintViolation<ItemRequestDtoForCreate>> violations = validator.validate(dto);

        assertThat(violations).isEmpty();
    }
}