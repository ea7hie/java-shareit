package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@AllArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private BookingService bookingService;

    /*@GetMapping("/{id}")
    public BookingDto getById(@PathVariable long id) {
        return bookingService.getById(id);
    }

    @GetMapping
    public BookingDto getByOwner(@RequestHeader("X-Sharer-User-Id") long id) {
        return bookingService.getByOwner(id);
    }*/

    @PostMapping
    public BookingDto createBooking(@Valid @RequestBody BookingDto bookingDto) {
        return bookingService.createBooking(bookingDto);
    }

   /* @GetMapping
    public BookingDto get(@Valid @RequestBody BookingDto bookingDto) {
        return bookingService.createBooking(bookingDto);
    }

    @DeleteMapping
    public BookingDto get(@Valid @RequestBody BookingDto bookingDto) {
        return bookingService.createBooking(bookingDto);
    }*/
}
