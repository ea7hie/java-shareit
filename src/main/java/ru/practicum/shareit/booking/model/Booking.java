package ru.practicum.shareit.booking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    private long id;

    @FutureOrPresent
    @Column(name = "time_start")
    private LocalDate start;

    @FutureOrPresent
    @Column(name = "time_end")
    private LocalDate end;

    @Column(name = "item_id")
    private long itemId;

    @Column(name = "booker_id")
    private long bookerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BookingStatus status;
}