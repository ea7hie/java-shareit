package ru.practicum.shareit.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.Collection;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Collection<Booking> findAllByItemId(long itemId);

    Collection<Booking> findAllByItemIdAndStatus(long itemId, BookingStatus status);

    Collection<Booking> findAllByBookerIdAndStatus(long bookerId, BookingStatus status);

    Collection<Booking> findAllByBookerId(long bookerId);

    Collection<Booking> findAllByBookerIdAndEndBefore(long bookerId, LocalDateTime start);

    Collection<Booking> findAllByBookerIdAndStartAfter(long bookerId, LocalDateTime end);

    Collection<Booking> findByBookerIdAndStartBeforeAndEndAfter(long bookerId, LocalDateTime start, LocalDateTime end);

    Collection<Booking> findAllByItemOwnerIdAndStatus(long ownerId, BookingStatus status);

    Collection<Booking> findAllByItemOwnerId(long ownerId);

    Collection<Booking> findAllByItemOwnerIdAndEndBefore(long ownerId, LocalDateTime start);

    Collection<Booking> findAllByItemOwnerIdAndStartAfter(long ownerId, LocalDateTime end);

    Collection<Booking> findByItemOwnerIdAndStartBeforeAndEndAfter(long ownerId, LocalDateTime start, LocalDateTime end);

    @Modifying
    @Query("UPDATE Booking b SET b.status = :status WHERE b.id = :bookingId")
    void updateBooking(@Param("bookingId") long bookingId, @Param("status") BookingStatus status);
}
