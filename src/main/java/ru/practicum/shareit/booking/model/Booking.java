package ru.practicum.shareit.booking.model;

import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Booking {

    private long id;

    @FutureOrPresent
    private LocalDate start;

    @FutureOrPresent
    private LocalDate end;

    private long itemId;


    private long bookerId;

    @Enumerated
    private BookingStatus status;
}