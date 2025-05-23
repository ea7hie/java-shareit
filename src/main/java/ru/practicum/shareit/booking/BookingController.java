package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.util.Collection;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;
    private final String headerOfUserId = "X-Sharer-User-Id";


    @PostMapping
    public BookingDto createBooking(@Valid @RequestBody BookingDto bookingDto) {
        return bookingService.createBooking(bookingDto);
    }

    @GetMapping
    public Collection<BookingDto> getAllBookingsByStatus(@RequestParam(required = false) BookingStatus status) {
        return bookingService.getAllBookingsByStatus(status);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@PathVariable long bookingId) {
        return bookingService.getBookingById(bookingId);
    }

    @GetMapping("/item/{itemId}")
    public Collection<BookingDto> getAllBookingsByItemIdAndStatus(@PathVariable long itemId,
                                                                  @RequestParam(required = false) BookingStatus status) {
        return bookingService.getAllBookingsByItemIdAndStatus(itemId, status);
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
    public BookingDto updateBooking(@RequestBody BookingDto booking, @RequestHeader(headerOfUserId) long userId,
                                    @PathVariable long bookingId) {
        return bookingService.updateBooking(booking, userId, bookingId);
    }

    @DeleteMapping("/{bookingId}")
    public BookingDto deleteBooking(@PathVariable long bookingId, @RequestHeader(headerOfUserId) long userId) {
        return bookingService.deleteBooking(bookingId, userId);
    }
}
