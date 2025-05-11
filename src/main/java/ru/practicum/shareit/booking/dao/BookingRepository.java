package ru.practicum.shareit.booking.dao;

import ru.practicum.shareit.booking.model.Booking;

public interface BookingRepository {
    Booking createBooking(Booking booking);
    Booking getBooking(long bookingIg);
    Booking updateBooking(Booking bookingForUpdate);
    Booking deleteBooking(long bookingIdForDelete);
}
