package ru.practicum.shareit.booking.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class BookingTest {
    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testBookingCreationAndAccessors() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.plusDays(1);
        LocalDateTime end = now.plusDays(2);

        Item item = new Item();
        item.setId(10L);

        User user = new User();
        user.setId(20L);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(start);
        booking.setEnd(end);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.APPROVED);

        assertThat(booking.getId()).isEqualTo(1L);
        assertThat(booking.getStart()).isEqualTo(start);
        assertThat(booking.getEnd()).isEqualTo(end);
        assertThat(booking.getItem()).isEqualTo(item);
        assertThat(booking.getBooker()).isEqualTo(user);
        assertThat(booking.getStatus()).isEqualTo(BookingStatus.APPROVED);
    }

    @Test
    void whenEndIsBeforeNow_thenValidationFails() {
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().minusDays(1));

        Set<ConstraintViolation<Booking>> violations = validator.validate(booking);

        assertThat(violations).isNotEmpty();
        boolean foundFutureViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("end") && v.getMessage().contains("must be a future date"));
        assertThat(!foundFutureViolation).isTrue();
    }

    @Test
    void whenStartIsInPast_thenValidationFails() {
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now().minusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));

        Set<ConstraintViolation<Booking>> violations = validator.validate(booking);

        assertThat(violations).isNotEmpty();
        boolean foundFutureOrPresentViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("start") && v.getMessage().contains("must be a date in the present or in the future"));
        assertThat(!foundFutureOrPresentViolation).isTrue();
    }
}