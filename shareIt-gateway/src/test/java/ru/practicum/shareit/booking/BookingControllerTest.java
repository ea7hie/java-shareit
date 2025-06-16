package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BookingControllerTest {

    @Mock
    private BookingClient bookingClient;

    @InjectMocks
    private BookingController bookingController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createBookingGW_shouldCallClient() {
        long userId = 1L;
        BookItemRequestDto dto = new BookItemRequestDto();
        ResponseEntity<Object> response = ResponseEntity.ok("created");

        when(bookingClient.createBooking(userId, dto)).thenReturn(response);

        ResponseEntity<Object> result = bookingController.createBookingGW(userId, dto);

        verify(bookingClient).createBooking(userId, dto);
        assertEquals(response, result);
    }

    @Test
    void getBookingsGW_shouldParseParamsAndCallClient() {
        long userId = 1L;
        String state = "CURRENT";
        int from = 0;
        int size = 10;
        ResponseEntity<Object> response = ResponseEntity.ok("ok");

        when(bookingClient.getBookings(userId, BookingState.CURRENT, from, size)).thenReturn(response);

        ResponseEntity<Object> result = bookingController.getBookingsGW(userId, state, from, size);

        verify(bookingClient).getBookings(userId, BookingState.CURRENT, from, size);
        assertEquals(response, result);
    }

    @Test
    void getBookingsGW_shouldThrowExceptionOnInvalidState() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> bookingController.getBookingsGW(1L, "INVALID", 0, 10)
        );
        assertTrue(ex.getMessage().contains("Unknown state"));
    }

    @Test
    void getAllBookingsByStatusGW_shouldWork() {
        when(bookingClient.getAllBookingsByStatus(1L, BookingState.ALL))
                .thenReturn(ResponseEntity.ok("all"));

        ResponseEntity<Object> result = bookingController.getAllBookingsByStatusGW(1L, "all");

        verify(bookingClient).getAllBookingsByStatus(1L, BookingState.ALL);
        assertEquals("all", result.getBody());
    }

    @Test
    void getAllBookingsByStatusForOwnerGW_shouldWork() {
        when(bookingClient.getAllBookingsByStatusForOwner(1L, BookingState.PAST))
                .thenReturn(ResponseEntity.ok("past"));

        ResponseEntity<Object> result = bookingController.getAllBookingsByStatusForOwnerGW(1L, "PAST");

        verify(bookingClient).getAllBookingsByStatusForOwner(1L, BookingState.PAST);
        assertEquals("past", result.getBody());
    }

    @Test
    void getBookingGW_shouldWork() {
        when(bookingClient.getBooking(1L, 2L)).thenReturn(ResponseEntity.ok("booking"));

        ResponseEntity<Object> result = bookingController.getBookingGW(1L, 2L);

        verify(bookingClient).getBooking(1L, 2L);
        assertEquals("booking", result.getBody());
    }

    @Test
    void getAllBookingsByItemIdAndStatusGW_shouldWork() {
        when(bookingClient.getAllBookingsByItemIdAndStatus(1L, 5L, BookingState.REJECTED))
                .thenReturn(ResponseEntity.ok("item"));

        ResponseEntity<Object> result = bookingController.getAllBookingsByItemIdAndStatusGW(1L, 5L, "REJECTED");

        verify(bookingClient).getAllBookingsByItemIdAndStatus(1L, 5L, BookingState.REJECTED);
        assertEquals("item", result.getBody());
    }

    @Test
    void getAllBookingsFromBookerGW_shouldWork() {
        when(bookingClient.getAllBookingsFromBooker(22L, 1L))
                .thenReturn(ResponseEntity.ok("booker"));

        ResponseEntity<Object> result = bookingController.getAllBookingsFromBookerGW(1L, 22L);

        verify(bookingClient).getAllBookingsFromBooker(22L, 1L);
        assertEquals("booker", result.getBody());
    }

    @Test
    void updateBookingGW_shouldWork() {
        when(bookingClient.updateBooking(2L, 1L, true)).thenReturn(ResponseEntity.ok("updated"));

        ResponseEntity<Object> result = bookingController.updateBookingGW(1L, 2L, true);

        verify(bookingClient).updateBooking(2L, 1L, true);
        assertEquals("updated", result.getBody());
    }

    @Test
    void deleteBookingGW_shouldWork() {
        when(bookingClient.deleteBooking(1L, 3L)).thenReturn(ResponseEntity.ok("deleted"));

        ResponseEntity<Object> result = bookingController.deleteBookingGW(1L, 3L);

        verify(bookingClient).deleteBooking(1L, 3L);
        assertEquals("deleted", result.getBody());
    }
}