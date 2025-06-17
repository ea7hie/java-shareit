package ru.practicum.shareit.user;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldCreateItemCorrectly() {
        User user = new User();
        user.setId(1L);
        user.setName("User");
        user.setEmail("user2@example.com");

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getName()).isEqualTo("User");
        assertThat(user.getEmail()).isEqualTo("user2@example.com");
    }

    @Test
    void shouldDetectBlankName() {
        User user = new User(0L, " ", "user2@example.com");
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }


    @Test
    void shouldPassValidationWithValidFields() {
        User user = new User(10L, "me", "user2@example.com");
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertThat(violations).isEmpty();
    }

    @Test
    void testIsEmailValid() {
        assertTrue(isEmailValid("valid@example.com"));
        assertFalse(isEmailValid("invalid-email"));
        assertFalse(isEmailValid(null));
    }

    private boolean isEmailValid(String email) {
        if (email == null) {
            return false;
        }
        return email.matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$");
    }

}