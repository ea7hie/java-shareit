package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.model.Booking;

import java.util.Comparator;

public class BookingComparatorByStartDesc implements Comparator<Booking> {
    @Override
    public int compare(Booking b1, Booking b2) {
        return b1.getStart().compareTo(b2.getStart());
    }
}
