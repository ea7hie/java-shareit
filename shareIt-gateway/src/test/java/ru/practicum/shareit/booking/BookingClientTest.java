package ru.practicum.shareit.booking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

//@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
//@SpringBootTest
//@ContextConfiguration(classes = BookingClient.class)
@SpringJUnitConfig( {BookingClient.class})
class BookingClientTest {
    @Autowired
    BookingClient bookingClient;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void createBooking_whenBookItemRequestDtoValid() throws JsonProcessingException {
        LocalDateTime now = LocalDateTime.now();
        BookItemRequestDto bookItemRequestDto = new BookItemRequestDto(1L, now, now.plusDays(2));

        ResponseEntity<Object> response = bookingClient.createBooking(1L, bookItemRequestDto);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(response.getBody(), objectMapper.writeValueAsString(bookItemRequestDto));


    }

    @Test
    void getBookings() {
    }

    @Test
    void getAllBookingsByStatus() {
    }

    @Test
    void getAllBookingsByStatusForOwner() {
    }

    @Test
    void getBooking() {
    }

    @Test
    void getAllBookingsByItemIdAndStatus() {
    }

    @Test
    void getAllBookingsFromBooker() {
    }

    @Test
    void updateBooking() {
    }

    @Test
    void deleteBooking() {
    }
}