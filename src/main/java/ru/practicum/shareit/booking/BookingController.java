package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoPost;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.enums.BookingDtoStates;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;
    private final String headerOfUserId = "X-Sharer-User-Id";


    @PostMapping
    public BookingDto createBooking(@Valid @RequestBody BookingDtoPost bookingDtoPost,
                                    @RequestHeader(headerOfUserId) long userId) {
        return bookingService.createBooking(bookingDtoPost, userId);
    }

    @GetMapping
    public Collection<BookingDto> getAllBookingsByStatus(@RequestHeader(headerOfUserId) long userId,
                                                         @RequestParam(required = false) BookingDtoStates state) {
        return bookingService.getAllBookingsByStatus(userId, state);
    }

    @GetMapping("/owner")
    public Collection<BookingDto> getAllBookingsByStatusForOwner(@RequestHeader(headerOfUserId) long userId,
                                                                 @RequestParam(required = false) BookingDtoStates state) {
        return bookingService.getAllBookingsByStatusForOwner(userId, state);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@PathVariable long bookingId, @RequestHeader(headerOfUserId) long userId) {
        return bookingService.getBookingById(bookingId, userId);
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

    @PatchMapping("/{bookingId}")
    public BookingDto updateBooking(@PathVariable long bookingId, @RequestHeader(headerOfUserId) long userId,
                                    @RequestParam Boolean approved) {
        return bookingService.updateBooking(bookingId, userId, approved);
    }

    @DeleteMapping("/{bookingId}")
    public BookingDto deleteBooking(@PathVariable long bookingId, @RequestHeader(headerOfUserId) long userId) {
        return bookingService.deleteBooking(bookingId, userId);
    }
}
