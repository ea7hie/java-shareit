package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.util.Collection;

public interface BookingService {
    BookingDto createBooking(BookingDto bookingDto);

    Collection<BookingDto> getAllBookings();

    BookingDto getBookingById(long bookingId);

    Collection<BookingDto> getAllBookingsByItemId(long itemId);

    Collection<BookingDto> getAllBookingsByItemIdAndStatus(long itemId, BookingStatus bookingStatus);

    Collection<BookingDto> getAllBookingsByStatus(BookingStatus bookingStatus);

    Collection<BookingDto> getAllBookingsFromBooker(long bookerId);

    Collection<BookingDto> getAllBookingsToOwner(long ownerId);

    BookingDto updateBooking(BookingDto bookingForUpdate, long userId);

    BookingDto deleteBooking(long bookingIdForDelete);
}
