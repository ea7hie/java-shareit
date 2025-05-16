package ru.practicum.shareit.booking.dao;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.util.Collection;

public interface BookingRepository {
    Booking createBooking(Booking booking);

    Collection<Booking> getAllBookings();

    Booking getBookingById(long bookingIg);

    Collection<Booking> getAllBookingsByItemId(long itemId);

    Collection<Booking> getAllBookingsByItemIdAndStatus(long itemId, BookingStatus bookingStatus);

    Collection<Booking> getAllBookingsByStatus(BookingStatus bookingStatus);

    Collection<Booking> getAllBookingsFromBooker(long bookerId);

    Collection<Booking> getAllBookingsToOwner(long ownerId);

    Booking updateBooking(BookingDto bookingDtoForUpdate, long userId);

    Booking deleteBooking(long bookingIdForDelete, long userId);
}
