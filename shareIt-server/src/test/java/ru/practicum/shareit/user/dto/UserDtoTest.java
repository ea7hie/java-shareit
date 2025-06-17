package ru.practicum.shareit.user.dto;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserDtoTest {

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
    void testValidUserDto() {
        UserDto userDto = new UserDto(1L, "Иван", "ivan@example.com");
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        assertTrue(violations.isEmpty(), "Ошибок валидации быть не должно");
    }

    @Test
    void testNameNotBlank() {
        UserDto userDto = new UserDto(1L, "  ", "ivan@example.com");
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void testEmailNotNull() {
        UserDto userDto = new UserDto(1L, "Иван", null);
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void testEmailValidFormat() {
        UserDto userDto = new UserDto(1L, "Иван", "invalid-email");
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void testGettersAndSetters() {
        UserDto userDto = new UserDto();
        userDto.setId(5L);
        userDto.setName("Пётр");
        userDto.setEmail("petr@example.com");

        assertEquals(5L, userDto.getId());
        assertEquals("Пётр", userDto.getName());
        assertEquals("petr@example.com", userDto.getEmail());
    }
}