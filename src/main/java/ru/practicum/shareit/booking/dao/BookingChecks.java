package ru.practicum.shareit.booking.dao;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.enums.Actions;
import ru.practicum.shareit.exception.model.NotFoundException;

import java.util.Optional;

@UtilityClass
public class BookingChecks {
    public void isBookingExistsById(BookingRepository bookingRepository, long bookingIdForCheck) {
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingIdForCheck);
        if (optionalBooking.isEmpty()) {
            throw new NotFoundException(String.format("Бронирования с id = %d для %s не найдено", bookingIdForCheck,
                    Actions.TO_VIEW));
        }
    }

    public Booking getBookingOrThrow(BookingRepository bookingRepository, long bookingId, String message) {
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isEmpty()) {
            throw new NotFoundException(String.format("Бронирования с id = %d для %s не найдено", bookingId, message));
        }
        return optionalBooking.get();
    }
}
