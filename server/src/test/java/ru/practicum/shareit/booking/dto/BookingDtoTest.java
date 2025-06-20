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
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingDtoTest {
    private final JacksonTester<BookingDto> json;

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testBookItemRequestDto() throws Exception {
        ItemDto item = new ItemDto();
        UserDto user = new UserDto();

        LocalDateTime now = LocalDateTime.now();
        BookingDto bookItemRequestDto = new BookingDto(1L, now, now.plusDays(2), item, user, BookingStatus.WAITING);

        JsonContent<BookingDto> result = json.write(bookItemRequestDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
    }

    @Test
    void shouldCreateBookingDtoSuccessfully() {
        LocalDateTime now = LocalDateTime.now().plusMinutes(10);
        BookingDto bookingDto = new BookingDto(
                1L,
                now,
                now.plusHours(1),
                new ItemDto(), // можно передать мок или stub
                new UserDto(),
                BookingStatus.WAITING
        );

        Set<ConstraintViolation<BookingDto>> violations = validator.validate(bookingDto);

        assertTrue(violations.isEmpty(), "BookingDto должен быть валиден при корректных данных");
    }

    @Test
    void shouldFailValidationWhenStartInPast() {
        BookingDto bookingDto = new BookingDto(
                1L,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1),
                new ItemDto(),
                new UserDto(),
                BookingStatus.WAITING
        );

        Set<ConstraintViolation<BookingDto>> violations = validator.validate(bookingDto);

        assertFalse(violations.isEmpty(), "Должна быть ошибка валидации, если start в прошлом");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("start")));
    }

    @Test
    void shouldFailValidationWhenEndInPast() {
        BookingDto bookingDto = new BookingDto(
                1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().minusDays(1),
                new ItemDto(),
                new UserDto(),
                BookingStatus.WAITING
        );

        Set<ConstraintViolation<BookingDto>> violations = validator.validate(bookingDto);

        assertFalse(violations.isEmpty(), "Должна быть ошибка валидации, если end в прошлом");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("end")));
    }

    @Test
    void shouldCorrectlyStoreFields() {
        long id = 42L;
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusHours(2);
        ItemDto item = new ItemDto();
        UserDto booker = new UserDto();
        BookingStatus status = BookingStatus.APPROVED;

        BookingDto bookingDto = new BookingDto(id, start, end, item, booker, status);

        assertEquals(id, bookingDto.getId());
        assertEquals(start, bookingDto.getStart());
        assertEquals(end, bookingDto.getEnd());
        assertEquals(item, bookingDto.getItem());
        assertEquals(booker, bookingDto.getBooker());
        assertEquals(status, bookingDto.getStatus());
    }

    @Test
    void testEqualsAndHashCode() {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusHours(1);
        ItemDto item = new ItemDto();
        UserDto booker = new UserDto();
        BookingStatus status = BookingStatus.WAITING;

        BookingDto dto1 = new BookingDto(1L, start, end, item, booker, status);
        BookingDto dto2 = new BookingDto(1L, start, end, item, booker, status);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToStringDoesNotThrow() {
        BookingDto dto = new BookingDto(
                1L,
                LocalDateTime.now().plusMinutes(5),
                LocalDateTime.now().plusHours(1),
                new ItemDto(),
                new UserDto(),
                BookingStatus.REJECTED
        );

        assertDoesNotThrow(() -> Objects.requireNonNull(dto.toString()));
    }

    @Test
    void shouldAllowNullItemBookerStatus() {
        LocalDateTime now = LocalDateTime.now().plusHours(1);
        BookingDto dto = new BookingDto(
                1L,
                now,
                now.plusHours(1),
                null,
                null,
                null
        );

        Set<ConstraintViolation<BookingDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty(), "Допустимо, если item, booker и status — null, если нет @NotNull");
    }

    @Test
    void shouldAllowStartEqualsEndIfInFuture() {
        LocalDateTime future = LocalDateTime.now().plusHours(2);
        BookingDto dto = new BookingDto(
                1L,
                future,
                future,
                new ItemDto(),
                new UserDto(),
                BookingStatus.APPROVED
        );

        Set<ConstraintViolation<BookingDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty(), "start == end допустим, если оба в будущем");
    }
}