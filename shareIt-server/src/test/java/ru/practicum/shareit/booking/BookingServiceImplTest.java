package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoPost;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.model.AccessError;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private final long userId = 1L;
    private final long bookingId = 10L;
    private final long itemId = 2L;

    private User user;
    private Item item;
    private Booking booking;
    private BookingDtoPost bookingDtoPost;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(userId);
        user.setName("User");
        user.setEmail("user@example.com");

        item = new Item();
        item.setId(itemId);
        item.setOwnerId(userId);
        item.setName("Item 1");
        item.setRequestId(0L);
        item.setAvailable(true);

        booking = new Booking();
        booking.setId(bookingId);
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);

        bookingDtoPost = new BookingDtoPost();
        bookingDtoPost.setItemId(itemId);
        bookingDtoPost.setStart(LocalDateTime.now().plusDays(1));
        bookingDtoPost.setEnd(LocalDateTime.now().plusDays(2));
    }

    @Test
    void createBooking_shouldCreateBookingSuccessfully() {
        item.setOwnerId(99L);

        booking.setStart(bookingDtoPost.getStart());
        booking.setEnd(bookingDtoPost.getEnd());
        booking.setItem(item);
        booking.setBooker(user);

        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(bookingRepository.findAllByItemIdAndStatus(itemId, BookingStatus.APPROVED)).thenReturn(List.of());
        Mockito.when(bookingRepository.save(Mockito.any(Booking.class))).thenReturn(booking);
        Mockito.when(commentRepository.findAllByItemId(itemId)).thenReturn(List.of());

        BookingDto result = bookingService.createBooking(bookingDtoPost, userId);

        assertNotNull(result);
        assertEquals(itemId, result.getItem().getId());
        assertEquals(userId, result.getBooker().getId());
        assertEquals(BookingStatus.WAITING, result.getStatus());
    }

    @Test
    void updateBooking_shouldUpdateBookingStatusByOwner() {
        item.setId(10L);
        //user.setId(200L);

        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);

        Mockito.when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        Mockito.when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        Mockito.when(commentRepository.findAllByItemId(item.getId())).thenReturn(List.of());

        BookingDto result = bookingService.updateBooking(bookingId, userId, true);

        assertNotNull(result);
        assertEquals(BookingStatus.APPROVED, result.getStatus());
        Mockito.verify(bookingRepository).updateBooking(bookingId, BookingStatus.APPROVED);
    }

    @Test
    void updateBooking_shouldThrowAccessError_whenUserIsNotOwner() {
        long bookingId = 1L;
        long notOwnerId = 300L;

        Item item = new Item();
        item.setId(10L);
        item.setOwnerId(100L);
        item.setRequestId(0L);

        User booker = new User();
        booker.setId(200L);

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);

        Mockito.when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        Mockito.when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        AccessError ex = assertThrows(AccessError.class, () -> {
            bookingService.updateBooking(bookingId, notOwnerId, true);
        });

        assertEquals("У вас нет прав доступа к редактированию этой брони.", ex.getMessage());
        Mockito.verify(bookingRepository, Mockito.never()).updateBooking(Mockito.anyLong(), Mockito.any());
    }

    @Test
    void getBookingById_shouldReturnBooking_whenUserIsBooker() {
        long bookingId = 1L;
        long userId = 10L; // booker

        Item item = new Item();
        item.setId(2L);
        item.setOwnerId(99L);
        item.setRequestId(0L);

        User booker = new User();
        booker.setId(userId);

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(booker));
        Mockito.when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        Mockito.when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        Mockito.when(commentRepository.findAllByItemId(item.getId())).thenReturn(List.of());

        BookingDto result = bookingService.getBookingById(bookingId, userId);

        assertNotNull(result);
        assertEquals(bookingId, result.getId());
        assertEquals(userId, result.getBooker().getId());
    }

    @Test
    void getBookingById_shouldReturnBooking_whenUserIsItemOwner() {
        long bookingId = 1L;
        long ownerId = 99L;

        Item item = new Item();
        item.setId(2L);
        item.setOwnerId(ownerId);
        item.setRequestId(0L);

        User booker = new User();
        booker.setId(10L);

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.APPROVED);

        Mockito.when(userRepository.findById(ownerId)).thenReturn(Optional.of(new User()));
        Mockito.when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        Mockito.when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        Mockito.when(commentRepository.findAllByItemId(item.getId())).thenReturn(List.of());

        BookingDto result = bookingService.getBookingById(bookingId, ownerId);

        assertNotNull(result);
        assertEquals(bookingId, result.getId());
        assertEquals(booker.getId(), result.getBooker().getId());
    }

    @Test
    void getBookingById_shouldThrowAccessError_whenUserIsNeitherOwnerNorBooker() {
        long bookingId = 1L;
        long strangerId = 55L;

        Item item = new Item();
        item.setId(2L);
        item.setOwnerId(99L);
        item.setRequestId(0L);

        User booker = new User();
        booker.setId(10L);

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);

        Mockito.when(userRepository.findById(strangerId)).thenReturn(Optional.of(new User()));
        Mockito.when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        Mockito.when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        AccessError ex = assertThrows(AccessError.class, () -> {
            bookingService.getBookingById(bookingId, strangerId);
        });

        assertEquals("У вас нет прав доступа к просмотру этой брони.", ex.getMessage());
    }

    @Test
    void isTimeOverlaps_shouldReturnTrue_whenBookingOverlaps() {
        long itemId = 1L;

        Booking existingBooking = new Booking();
        existingBooking.setStart(LocalDateTime.of(2025, 6, 10, 10, 0));
        existingBooking.setEnd(LocalDateTime.of(2025, 6, 12, 10, 0));
        Item item = new Item();
        item.setId(itemId);
        existingBooking.setItem(item);
        existingBooking.setStatus(BookingStatus.APPROVED);

        Booking newBooking = new Booking();
        newBooking.setStart(LocalDateTime.of(2025, 6, 11, 12, 0)); // пересекается
        newBooking.setEnd(LocalDateTime.of(2025, 6, 13, 10, 0));
        newBooking.setItem(item);

        Mockito.when(bookingRepository.findAllByItemIdAndStatus(itemId, BookingStatus.APPROVED))
                .thenReturn(List.of(existingBooking));

        boolean result = bookingService.isTimeOverlaps(newBooking);

        assertTrue(result);
    }

    @Test
    void isTimeOverlaps_shouldReturnFalse_whenNoExistingBookings() {
        long itemId = 1L;

        Booking newBooking = new Booking();
        newBooking.setStart(LocalDateTime.of(2025, 6, 10, 10, 0));
        newBooking.setEnd(LocalDateTime.of(2025, 6, 12, 10, 0));
        Item item = new Item();
        item.setId(itemId);
        newBooking.setItem(item);

        Mockito.when(bookingRepository.findAllByItemIdAndStatus(itemId, BookingStatus.APPROVED))
                .thenReturn(List.of());

        boolean result = bookingService.isTimeOverlaps(newBooking);

        assertFalse(result);
    }

    @Test
    void deleteBooking_shouldDeleteBooking_whenUserIsBooker() {
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        Mockito.when(commentRepository.findAllByItemId(item.getId())).thenReturn(List.of());

        BookingDto result = bookingService.deleteBooking(bookingId, userId);

        Mockito.verify(bookingRepository).deleteById(bookingId);
        assertEquals(booking.getId(), result.getId());
    }

    @Test
    void deleteBooking_shouldThrowNotFoundException_whenUserNotFound() {
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                bookingService.deleteBooking(bookingId, userId));
    }

    @Test
    void deleteBooking_shouldThrowAccessError_whenUserIsNotBooker() {
        User anotherUser = new User();
        anotherUser.setId(99L);
        booking.setBooker(anotherUser);

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        assertThrows(AccessError.class, () ->
                bookingService.deleteBooking(bookingId, userId));
    }
}