package ru.practicum.shareit.item.comment;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommentDtoTest {
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
    void whenAuthorNameIsBlank_thenValidationFails() {
        CommentDto commentDto = new CommentDto(0L, "Text", "", LocalDateTime.now());
        Set<ConstraintViolation<CommentDto>> violations = validator.validate(commentDto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("authorName")));
    }

    @Test
    void whenTextIsBlank_thenValidationFails() {
        CommentDto commentDto = new CommentDto(0L, "", "user", LocalDateTime.now());
        Set<ConstraintViolation<CommentDto>> violations = validator.validate(commentDto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("text")));
    }

    @Test
    void whenTextIsNull_thenValidationFails() {
        CommentDto commentDto = new CommentDto(0L, null, "user", LocalDateTime.now());
        Set<ConstraintViolation<CommentDto>> violations = validator.validate(commentDto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("text")));
    }

}