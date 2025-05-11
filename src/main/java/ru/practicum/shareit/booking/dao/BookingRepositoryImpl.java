package ru.practicum.shareit.booking.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;

import java.util.HashMap;
import java.util.Map;

@Repository
public class BookingRepositoryImpl implements BookingRepository {
    private Map<Long, Booking> allBookingsById = new HashMap<>();
    private long id = 0;

    @Override
    public Booking createBooking(Booking booking) {
        booking.setId(getNewId());
        allBookingsById.put(booking.getId(), booking);
        return booking;
    }

    @Override
    public Booking getBooking(long bookingIg) {
        return null;
    }

    @Override
    public Booking updateBooking(Booking bookingForUpdate) {
        return null;
    }

    @Override
    public Booking deleteBooking(long bookingIdForDelete) {
        return null;
    }

    private long getNewId() {
        return ++id;
    }
}
