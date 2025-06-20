package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoPost;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.enums.BookingDtoStates;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createBooking_ShouldReturnBookingDto() {
        BookingDtoPost bookingDtoPost = new BookingDtoPost();
        long userId = 1L;
        BookingDto expectedBookingDto = new BookingDto();

        when(bookingService.createBooking(bookingDtoPost, userId)).thenReturn(expectedBookingDto);

        BookingDto result = bookingController.createBooking(bookingDtoPost, userId);

        assertNotNull(result);
        assertEquals(expectedBookingDto, result);
        verify(bookingService, times(1)).createBooking(bookingDtoPost, userId);
    }

    @Test
    void getAllBookingsByStatusForOwnerInAmount_ShouldReturnCollection() {
        long userId = 1L;
        BookingDtoStates state = BookingDtoStates.ALL;
        int from = 0;
        int size = 10;
        List<BookingDto> expectedList = List.of(new BookingDto(), new BookingDto());

        when(bookingService.getAllBookingsByStatusForOwnerInAmount(userId, state, from, size))
                .thenReturn(expectedList);

        var result = bookingController.getAllBookingsByStatusForOwnerInAmount(userId, state, from, size);

        assertEquals(expectedList, result);
        verify(bookingService, times(1)).getAllBookingsByStatusForOwnerInAmount(userId, state, from, size);
    }

    @Test
    void getBookingById_ShouldReturnBookingDto() {
        long bookingId = 1L;
        long userId = 1L;
        BookingDto expectedBookingDto = new BookingDto();

        when(bookingService.getBookingById(bookingId, userId)).thenReturn(expectedBookingDto);

        BookingDto result = bookingController.getBookingById(bookingId, userId);

        assertEquals(expectedBookingDto, result);
        verify(bookingService, times(1)).getBookingById(bookingId, userId);
    }

    @Test
    void updateBooking_ShouldReturnBookingDto() {
        long bookingId = 1L;
        long userId = 1L;
        Boolean approved = true;
        BookingDto expectedBookingDto = new BookingDto();

        when(bookingService.updateBooking(bookingId, userId, approved)).thenReturn(expectedBookingDto);

        BookingDto result = bookingController.updateBooking(bookingId, userId, approved);

        assertEquals(expectedBookingDto, result);
        verify(bookingService, times(1)).updateBooking(bookingId, userId, approved);
    }

    @Test
    void deleteBooking_ShouldReturnBookingDto() {
        long bookingId = 1L;
        long userId = 1L;
        BookingDto expectedBookingDto = new BookingDto();

        when(bookingService.deleteBooking(bookingId, userId)).thenReturn(expectedBookingDto);

        BookingDto result = bookingController.deleteBooking(bookingId, userId);

        assertEquals(expectedBookingDto, result);
        verify(bookingService, times(1)).deleteBooking(bookingId, userId);
    }

    @Test
    void getAllBookingsByStatus_ShouldReturnCollection() {
        long userId = 1L;
        BookingDtoStates state = BookingDtoStates.CURRENT;
        List<BookingDto> expectedList = List.of(new BookingDto());

        when(bookingService.getAllBookingsByStatus(userId, state)).thenReturn(expectedList);

        var result = bookingController.getAllBookingsByStatus(userId, state);

        assertEquals(expectedList, result);
        verify(bookingService, times(1)).getAllBookingsByStatus(userId, state);
    }

    @Test
    void getAllBookingsByStatusForOwner_ShouldReturnCollection() {
        long userId = 1L;
        BookingDtoStates state = BookingDtoStates.PAST;
        List<BookingDto> expectedList = List.of(new BookingDto());

        when(bookingService.getAllBookingsByStatusForOwner(userId, state)).thenReturn(expectedList);

        var result = bookingController.getAllBookingsByStatusForOwner(userId, state);

        assertEquals(expectedList, result);
        verify(bookingService, times(1)).getAllBookingsByStatusForOwner(userId, state);
    }

    @Test
    void getAllBookingsByItemIdAndStatus_ShouldReturnCollection() {
        long itemId = 2L;
        BookingStatus status = BookingStatus.APPROVED;
        List<BookingDto> expectedList = List.of(new BookingDto());

        when(bookingService.getAllBookingsByItemIdAndStatus(itemId, status)).thenReturn(expectedList);

        var result = bookingController.getAllBookingsByItemIdAndStatus(itemId, status);

        assertEquals(expectedList, result);
        verify(bookingService, times(1)).getAllBookingsByItemIdAndStatus(itemId, status);
    }

    @Test
    void getAllBookingsFromBooker_ShouldReturnCollection() {
        long bookerId = 3L;
        List<BookingDto> expectedList = List.of(new BookingDto(), new BookingDto());

        when(bookingService.getAllBookingsFromBooker(bookerId)).thenReturn(expectedList);

        var result = bookingController.getAllBookingsFromBooker(bookerId);

        assertEquals(expectedList, result);
        verify(bookingService, times(1)).getAllBookingsFromBooker(bookerId);
    }
}