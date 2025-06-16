package ru.practicum.shareit.booking.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = BookItemRequestDto.class)
class BookItemRequestDtoTest {
    private final JacksonTester<BookItemRequestDto> json;

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testBookItemRequestDto() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        BookItemRequestDto bookItemRequestDto = new BookItemRequestDto(1L, now, now.plusDays(2));

        JsonContent<BookItemRequestDto> result = json.write(bookItemRequestDto);

        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
    }


    @Test
    void from_shouldReturnCorrectEnumForValidString() {
        assertEquals(Optional.of(BookingState.ALL), BookingState.from("ALL"));
        assertEquals(Optional.of(BookingState.CURRENT), BookingState.from("current"));
        assertEquals(Optional.of(BookingState.PAST), BookingState.from("PaSt"));
        assertEquals(Optional.of(BookingState.FUTURE), BookingState.from("fUtUrE"));
        assertEquals(Optional.of(BookingState.WAITING), BookingState.from("waiting"));
        assertEquals(Optional.of(BookingState.REJECTED), BookingState.from("REJECTED"));
    }

    @Test
    void from_shouldReturnEmptyOptionalForInvalidString() {
        assertEquals(Optional.empty(), BookingState.from("INVALID"));
        assertEquals(Optional.empty(), BookingState.from(""));
        assertEquals(Optional.empty(), BookingState.from("123"));
    }

    @Test
    void from_shouldReturnEmptyOptionalForNull() {
        assertEquals(Optional.empty(), BookingState.from(null));
    }

    @Test
    void validDto_shouldPassValidation() {
        BookItemRequestDto dto = new BookItemRequestDto(
                1L,
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2)
        );

        Set<ConstraintViolation<BookItemRequestDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty(), "Должно быть без ошибок валидации");
    }

    @Test
    void startInPast_shouldFailValidation() {
        BookItemRequestDto dto = new BookItemRequestDto(
                1L,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1)
        );

        Set<ConstraintViolation<BookItemRequestDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty(), "Ожидается ошибка валидации по полю start");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("start")));
    }

    @Test
    void endInPast_shouldFailValidation() {
        BookItemRequestDto dto = new BookItemRequestDto(
                1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().minusHours(1)
        );

        Set<ConstraintViolation<BookItemRequestDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty(), "Ожидается ошибка валидации по полю end");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("end")));
    }

    @Test
    void bothStartAndEndInvalid_shouldFailValidationForBothFields() {
        BookItemRequestDto dto = new BookItemRequestDto(
                1L,
                LocalDateTime.now().minusHours(2),
                LocalDateTime.now().minusHours(1)
        );

        Set<ConstraintViolation<BookItemRequestDto>> violations = validator.validate(dto);

        assertEquals(2, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("start")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("end")));
    }
}
