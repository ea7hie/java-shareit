package ru.practicum.shareit.booking.model;

import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.validations.CheckDate;

import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */

@Data
@AllArgsConstructor
public class Booking {
    private long id;

    @FutureOrPresent
    @CheckDate
    private LocalDate start;

    @FutureOrPresent
    @CheckDate
    private LocalDate end;

    /*private Item item;
    private User booker;
    private BookingStatus status;*/
}