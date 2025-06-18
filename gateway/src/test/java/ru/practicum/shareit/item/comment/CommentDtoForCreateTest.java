package ru.practicum.shareit.item.comment;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommentDtoForCreateTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldPassValidationWhenTextIsNotBlank() {
        CommentDtoForCreate dto = new CommentDtoForCreate("This is a comment");

        Set<ConstraintViolation<CommentDtoForCreate>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty(), "Validation should pass when text is not blank");
    }

    @Test
    void shouldFailValidationWhenTextIsNull() {
        CommentDtoForCreate dto = new CommentDtoForCreate(null);

        Set<ConstraintViolation<CommentDtoForCreate>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty(), "Validation should fail when text is null");
    }

    @Test
    void shouldFailValidationWhenTextIsEmpty() {
        CommentDtoForCreate dto = new CommentDtoForCreate("");

        Set<ConstraintViolation<CommentDtoForCreate>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty(), "Validation should fail when text is empty");
    }

    @Test
    void shouldFailValidationWhenTextIsBlank() {
        CommentDtoForCreate dto = new CommentDtoForCreate("   ");

        Set<ConstraintViolation<CommentDtoForCreate>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty(), "Validation should fail when text is blank");
    }
}