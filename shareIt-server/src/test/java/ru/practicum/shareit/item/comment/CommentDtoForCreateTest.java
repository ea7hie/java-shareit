package ru.practicum.shareit.item.comment;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommentDtoForCreateTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;


    @BeforeAll
    static void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        validatorFactory.close();
    }

    @Test
    void whenTextIsBlank_thenValidationFails() {
        CommentDtoForCreate dto = new CommentDtoForCreate("");
        Set<ConstraintViolation<CommentDtoForCreate>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("text")));
    }

    @Test
    void whenTextIsNull_thenValidationFails() {
        CommentDtoForCreate dto = new CommentDtoForCreate(null);
        Set<ConstraintViolation<CommentDtoForCreate>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("text")));
    }

    @Test
    void whenTextIsValid_thenValidationPasses() {
        CommentDtoForCreate dto = new CommentDtoForCreate("Valid comment text");
        Set<ConstraintViolation<CommentDtoForCreate>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }
}