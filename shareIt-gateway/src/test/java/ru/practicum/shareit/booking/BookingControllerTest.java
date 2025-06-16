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



/*
@WebMvcTest(BookingController.class)
@SpringJUnitConfig( {BookingController.class})
class BookingControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingClient bookingClient;

    @Autowired
    private MockMvc mockMvc;

    private BookItemRequestDto bookingCreateDto;
    private UserDto userDto;
    private ItemDto itemDto;
    private BookingDto bookingDto;

    @BeforeEach
    void setUp() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        userDto = new UserDto(1L, "First User", "ea7hie@ya.ru");
        itemDto = new ItemDto(1L, "Iphone", "Iphone 15 pro max", 3L, 0L, true, List.of());

        bookingCreateDto = new BookItemRequestDto(1L, start, end);
        bookingDto = new BookingDto(1L, start, end, itemDto, userDto, BookingStatus.WAITING);
    }

    @Test
    void createBookingGW_validBookItemRequestDto() throws Exception {
        String bookingJson = objectMapper.writeValueAsString(bookingCreateDto);
        ResponseEntity<Object> response = new ResponseEntity<>("", HttpStatus.OK);

        when(bookingClient.createBooking(1L, bookingCreateDto)).thenReturn(response);

        mockMvc.perform(post("/bookings")
                        .content(bookingJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());

        verify(bookingClient).createBooking(1L, bookingCreateDto);
    }

    @Test
    void createBookingGW_notValidBookItemRequestDto() throws Exception {
        BookItemRequestDto bookingCreateDtoNotValid = new BookItemRequestDto(1L, LocalDateTime.now(),
                LocalDateTime.now().minusMinutes(30));

        String bookingJson = objectMapper.writeValueAsString(bookingCreateDtoNotValid);
        ResponseEntity<Object> response = new ResponseEntity<>("", HttpStatus.BAD_REQUEST);

        when(bookingClient.createBooking(1L, bookingCreateDtoNotValid)).thenReturn(response);

        mockMvc.perform(post("/bookings")
                        .content(bookingJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isBadRequest());

        verify(bookingClient).createBooking(1L, bookingCreateDtoNotValid);
    }

    @Test
    void approveBooking() throws Exception {
        mockMvc.perform(patch("/bookings/{bookingId}?approved={approved}", 1, true)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());

        verify(bookingClient).updateBooking(1L, 1L, true);
    }

    @Test
    void getBookingById() throws Exception {
        mockMvc.perform(get("/bookings/{bookingId}", 1)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());

        verify(bookingClient).getBooking(1L, 1L);
    }
/*
    @Test
    void getBookingByUser() throws Exception {
        mockMvc.perform(get("/bookings?state={state}", BookingState.ALL)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());

        verify(bookingClient).getBookingByUser(any(), any());
    }

    @Test
    void getBookingItemByUser() throws Exception {
        mockMvc.perform(get("/bookings/owner?state={state}", BookingState.ALL)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());

        verify(bookingClient).getBookingItemByUser(any(), any());
    }*/
/*}*/



/*
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
                                                  @RequestParam("approved") Boolean approved) {
        return bookingClient.updateBooking(bookingId, userId, approved);
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<Object> deleteBookingGW(@RequestHeader(headerOfUserId) long userId,
                                                  @Positive @PathVariable long bookingId) {
        return bookingClient.deleteBooking(userId, bookingId);
    }

    */

