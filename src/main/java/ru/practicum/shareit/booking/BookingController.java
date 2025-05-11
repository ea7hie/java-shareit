package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.util.Collection;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@AllArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private BookingService bookingService;

    @PostMapping
    public BookingDto createBooking(@Valid @RequestBody BookingDto bookingDto) {
        return bookingService.createBooking(bookingDto);
    }

    @GetMapping
    public Collection<BookingDto> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@PathVariable long bookingId) {
        return bookingService.getBookingById(bookingId);
    }

    @GetMapping("/item/{itemId}")
    public Collection<BookingDto> getAllBookingsByItemId(@PathVariable long itemId) {
        return bookingService.getAllBookingsByItemId(itemId);
    }

    @GetMapping("/item/{itemId}?status={bookingStatus}")
    public Collection<BookingDto> getAllBookingsByItemIdAndStatus(@PathVariable long itemId,
                                                                  @RequestParam BookingStatus bookingStatus) {
        return bookingService.getAllBookingsByItemIdAndStatus(itemId, bookingStatus);
    }

    @GetMapping("?status={bookingStatus}")
    public Collection<BookingDto> getAllBookingsByStatus(@RequestParam BookingStatus bookingStatus) {
        return bookingService.getAllBookingsByStatus(bookingStatus);
    }

    @GetMapping("/booker/{bookerId}")
    public Collection<BookingDto> getAllBookingsFromBooker(@PathVariable long bookerId) {
        return bookingService.getAllBookingsFromBooker(bookerId);
    }

    @GetMapping("/owner/{ownerId}")
    public Collection<BookingDto> getAllBookingsToOwner(@PathVariable long ownerId) {
        return bookingService.getAllBookingsToOwner(ownerId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateBooking(@RequestBody BookingDto booking, @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.updateBooking(booking, userId);
    }

    @DeleteMapping("/{bookingId}")
    public BookingDto deleteBooking(@PathVariable long bookingId) {
        return bookingService.deleteBooking(bookingId);
    }
}
