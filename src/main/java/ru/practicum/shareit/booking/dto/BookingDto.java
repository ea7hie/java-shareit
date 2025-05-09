package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */

@Data
@AllArgsConstructor
public class BookingDto {
    private LocalDateTime start;
    private LocalDateTime end;
    private long itemId;
    private long bookerId;
    private BookingStatus status;
}
