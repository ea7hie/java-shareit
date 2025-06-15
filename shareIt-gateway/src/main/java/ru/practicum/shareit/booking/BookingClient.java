package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.exception.model.ValidationException;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> createBooking(long userId, BookItemRequestDto bookingDtoPost) {
        if (bookingDtoPost.getEnd().isBefore(bookingDtoPost.getStart()) ||
                bookingDtoPost.getEnd().isEqual(bookingDtoPost.getStart())) {
            throw new ValidationException("Окончание бронирования вещи не может быть раньше её начала " +
                    "или совпадать с ней.");
        }

        return post("", userId, bookingDtoPost);
    }

    public ResponseEntity<Object> getBookings(long userId, BookingState state, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return get("?state={state}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getAllBookingsByStatus(long userId, BookingState state) {
        Map<String, Object> parameters = Map.of("state", state.name());
        return get("/all?state={state}", userId, parameters);
    }

    public ResponseEntity<Object> getAllBookingsByStatusForOwner(long userId, BookingState state) {
        Map<String, Object> parameters = Map.of("state", state.name());
        return get("/owner?state={state}", userId, parameters);
    }

    public ResponseEntity<Object> getBooking(long userId, Long bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getAllBookingsByItemIdAndStatus(long userId, long itemId, BookingState state) {
        Map<String, Object> parameters = Map.of("state", state.name());
        return get("/item/" + itemId + "?state={state}", userId, parameters);
    }

    public ResponseEntity<Object> getAllBookingsFromBooker(long bookerId, long userId) {
        return get("/booker/" + bookerId, userId);
    }

    public ResponseEntity<Object> updateBooking(long bookingId, long userId, Boolean approved) {
        // Map<String, Object> parameters = Map.of("approved", approved);
        return patch("/" + bookingId + "?approved=" + approved, userId);
    }

    public ResponseEntity<Object> deleteBooking(long userId, long bookingId) {
        return delete("/" + bookingId, userId);
    }
}
