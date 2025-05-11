package ru.practicum.shareit.booking.model;

import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */

@Data
@AllArgsConstructor
public class Booking {
    private long id;

    @FutureOrPresent
    private LocalDate start;

    @FutureOrPresent
    private LocalDate end;

    private Item item;
    private User booker;
    private BookingStatus status;
}