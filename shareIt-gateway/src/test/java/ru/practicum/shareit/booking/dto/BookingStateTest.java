package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookingStateTest {

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
}