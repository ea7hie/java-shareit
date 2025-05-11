package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */

@Data
@AllArgsConstructor
public class BookingDto {
    private long id;

    @FutureOrPresent
    private LocalDate start;

    @FutureOrPresent
    private LocalDate end;

    private long itemId;
    private long bookerId;
    private BookingStatus status;
}
