package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

public interface BookingService {
    BookingDto createBooking(BookingDto bookingDto);
    BookingDto getBooking(long bookingDtoId);
    BookingDto updateBooking(BookingDto bookingDtoForUpdate);
    BookingDto deleteBooking(long bookingDtoIdForDelete);
}
