package ru.practicum.shareit.booking.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.enums.Actions;
import ru.practicum.shareit.exception.model.AccessError;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.item.dao.ItemChecks;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookingRepositoryImpl implements BookingRepository {
    private final Map<Long, Booking> allBookingsById = new HashMap<>();

    private final ItemRepository itemRepository;
    private long id = 0;

    private final String messageCantUpdate = "У вас нет прав доступа к редактированию этой брони.";
    private final String messageCantDelete = "У вас нет прав доступа к удалению этой брони.";

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
        return getBookingOrThrow(bookingId, Actions.TO_VIEW);
    }

    @Override
    public Collection<Booking> getAllBookingsByItemId(long itemId) {
        return allBookingsById.values().stream()
                .filter(booking -> booking.getItemId() == itemId)
                .toList();
    }

    @Override
    public Collection<Booking> getAllBookingsByItemIdAndStatus(long itemId, BookingStatus bookingStatus) {
        return allBookingsById.values().stream()
                .filter(booking -> booking.getItemId() == itemId)
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
                .filter(booking -> booking.getBookerId() == bookerId)
                .toList();
    }

    @Override
    public Collection<Booking> getAllBookingsToOwner(long ownerId) {
        return allBookingsById.values().stream()
                .filter(booking -> {
                    Item item = ItemChecks.getItemOrThrow(itemRepository, booking.getItemId(), Actions.TO_VIEW);
                    return item.getOwnerId() == ownerId;
                })
                .toList();
    }

    @Override
    public Booking updateBooking(BookingDto bookingDtoForUpdate, long userId) {
        Booking bookingForUpdate = getBookingOrThrow(bookingDtoForUpdate.getId(), Actions.TO_UPDATE);
        Item item = ItemChecks.getItemOrThrow(itemRepository, bookingDtoForUpdate.getItemId(), Actions.TO_UPDATE);

        if (bookingForUpdate.getBookerId() == userId) {
            bookingForUpdate.setStart(bookingDtoForUpdate.getStart() == null ?
                    bookingForUpdate.getStart() : bookingDtoForUpdate.getStart());
            bookingForUpdate.setEnd(bookingDtoForUpdate.getEnd() == null ?
                    bookingForUpdate.getEnd() : bookingDtoForUpdate.getEnd());
        } else if (item.getOwnerId() == userId) {
            bookingForUpdate.setStatus(bookingDtoForUpdate.getStatus() == null ?
                    bookingForUpdate.getStatus() : bookingDtoForUpdate.getStatus());
        } else {
            throw new AccessError(messageCantUpdate);
        }

        return allBookingsById.put(bookingForUpdate.getId(), bookingForUpdate);
    }

    @Override
    public Booking deleteBooking(long bookingIdForDelete, long userId) {
        Booking bookingForDelete = getBookingOrThrow(bookingIdForDelete, Actions.TO_DELETE);
        if (bookingForDelete.getBookerId() == userId) {
            return allBookingsById.remove(bookingIdForDelete);
        }
        throw new AccessError(messageCantDelete);
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