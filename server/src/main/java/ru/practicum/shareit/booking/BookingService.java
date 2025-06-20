package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoPost;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.enums.BookingDtoStates;

import java.util.Collection;

public interface BookingService {
    BookingDto createBooking(BookingDtoPost bookingDtoPost, long bookerId);

    Collection<BookingDto> getAllBookingsByStatusForOwnerInAmount(long userId, BookingDtoStates state,
                                                                  int from, int size);

    BookingDto getBookingById(long bookingId, long userId);

    Collection<BookingDto> getAllBookingsByItemId(long itemId);

    Collection<BookingDto> getAllBookingsByItemIdAndStatus(long itemId, BookingStatus bookingStatus);

    Collection<BookingDto> getAllBookingsByStatus(long userId, BookingDtoStates state);

    Collection<BookingDto> getAllBookingsFromBooker(long bookerId);

    Collection<BookingDto> getAllBookingsByStatusForOwner(long ownerId, BookingDtoStates state);

    BookingDto updateBooking(long bookingId, long userId, boolean approved);

    BookingDto deleteBooking(long bookingIdForDelete, long userId);
}
