package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;
    private final String headerOfUserId = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> createBookingGW(@RequestHeader(headerOfUserId) long userId,
                                                  @RequestBody @Valid BookItemRequestDto requestDto) {
        log.info("Creating booking {}, userId={}", requestDto, userId);
        return bookingClient.createBooking(userId, requestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getBookingsGW(@RequestHeader(headerOfUserId) long userId,
                                                @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                                @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getBookings(userId, state, from, size);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllBookingsByStatusGW(@RequestHeader(headerOfUserId) long userId,
                                                           @RequestParam(name = "state", defaultValue = "all")
                                                           String stateParam) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        return bookingClient.getAllBookingsByStatus(userId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllBookingsByStatusForOwnerGW(@RequestHeader(headerOfUserId) long userId,
                                                                   @RequestParam(name = "state", defaultValue = "all")
                                                                   String stateParam) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        return bookingClient.getAllBookingsByStatusForOwner(userId, state);
    }

    //original from shablone
    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingGW(@RequestHeader(headerOfUserId) long userId,
                                               @Positive @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping("/item/{itemId}")
    public ResponseEntity<Object> getAllBookingsByItemIdAndStatusGW(@RequestHeader(headerOfUserId) long userId,
                                                                    @Positive @PathVariable long itemId,
                                                                    @RequestParam(name = "state", defaultValue = "all")
                                                                    String stateParam) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        return bookingClient.getAllBookingsByItemIdAndStatus(userId, itemId, state);
    }

    @GetMapping("/booker/{bookerId}")
    public ResponseEntity<Object> getAllBookingsFromBookerGW(@RequestHeader(headerOfUserId) long userId,
                                                             @Positive @PathVariable long bookerId) {
        return bookingClient.getAllBookingsFromBooker(bookerId, userId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateBookingGW(@RequestHeader(headerOfUserId) long userId,
                                                  @Positive @PathVariable long bookingId,
                                                  @RequestParam Boolean approved) {
        return bookingClient.updateBooking(bookingId, userId, approved);
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<Object> deleteBookingGW(@RequestHeader(headerOfUserId) long userId,
                                                  @Positive @PathVariable long bookingId) {
        return bookingClient.deleteBooking(userId, bookingId);
    }
}
