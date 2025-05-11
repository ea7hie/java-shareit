package ru.practicum.shareit.booking.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.user.User;

import java.util.*;

@Repository
public class BookingRepositoryImpl implements BookingRepository {
    private final Map<Long, Booking> allBookingsById = new HashMap<>();
    private long id = 0;

    @Override
    public Booking createBooking(Booking booking) {
        booking.setId(getNewId());
        allBookingsById.put(booking.getId(), booking);
        return booking;
    }

    @Override
    public Collection<Booking> getAllBookings() {
        return allBookingsById.values();
    }

    @Override
    public Booking getBookingById(long bookingId) {
        return getBookingOrThrow(bookingId, "отображения");
    }

    @Override
    public Collection<Booking> getAllBookingsByItemId(long itemId) {
        return allBookingsById.values().stream()
                .filter(booking -> booking.getItem().getId() == itemId)
                .toList();
    }

    @Override
    public Collection<Booking> getAllBookingsByItemIdAndStatus(long itemId, BookingStatus bookingStatus) {
        return allBookingsById.values().stream()
                .filter(booking -> booking.getItem().getId() == itemId)
                .filter(booking -> booking.getStatus() == bookingStatus)
                .toList();
    }

    @Override
    public Collection<Booking> getAllBookingsByStatus(BookingStatus bookingStatus) {
        return allBookingsById.values().stream()
                .filter(booking -> booking.getStatus() == bookingStatus)
                .toList();
    }

    @Override
    public Collection<Booking> getAllBookingsFromBooker(long bookerId) {
        return allBookingsById.values().stream()
                .filter(booking -> booking.getBooker().getId() == bookerId)
                .toList();
    }

    @Override
    public Collection<Booking> getAllBookingsToOwner(long ownerId) {
        return allBookingsById.values().stream()
                .filter(booking -> booking.getItem().getOwner().getId() == ownerId)
                .toList();
    }

    @Override
    public Booking updateBooking(BookingDto bookingDtoForUpdate, long userId) {
        Booking bookingForUpdate = getBookingOrThrow(bookingDtoForUpdate.getId(), "обновления");

        if (bookingForUpdate.getBooker().getId() == userId) {
            bookingForUpdate.setStart(bookingDtoForUpdate.getStart() == null ?
                    bookingForUpdate.getStart() : bookingDtoForUpdate.getStart());
            bookingForUpdate.setEnd(bookingDtoForUpdate.getEnd() == null ?
                    bookingForUpdate.getEnd() : bookingDtoForUpdate.getEnd());
        }

        if (bookingForUpdate.getItem().getOwner().getId() == userId) {
            bookingForUpdate.setStatus(bookingDtoForUpdate.getStatus() == null ?
                    bookingForUpdate.getStatus() : bookingDtoForUpdate.getStatus());
        }

        return allBookingsById.put(bookingForUpdate.getId(), bookingForUpdate);
    }

    @Override
    public Booking deleteBooking(long bookingIdForDelete) {
        getBookingOrThrow(bookingIdForDelete, "удаления");
        return allBookingsById.remove(bookingIdForDelete);
    }

    private long getNewId() {
        return ++id;
    }

    private Booking getBookingOrThrow(long bookingId, String message) {
        Optional<Booking> optionalBooking = Optional.ofNullable(allBookingsById.get(bookingId));
        if (optionalBooking.isEmpty()) {
            throw new NotFoundException(String.format("Бронирования с id = %d для %s не найдено", bookingId, message));
        }
        return optionalBooking.get();
    }
}