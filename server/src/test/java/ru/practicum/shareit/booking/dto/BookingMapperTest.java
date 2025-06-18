package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookingMapperTest {

    @Test
    void toBookingDto_ShouldMapCorrectly() {
        Item item = new Item(1L, "ItemName", "Description", 1L, 0L, true);
        User booker = new User(2L, "user", "user@example.com");

        Booking booking = new Booking(
                10L,
                LocalDateTime.of(2025, 6, 16, 12, 0),
                LocalDateTime.of(2025, 6, 17, 12, 0),
                item,
                booker,
                BookingStatus.APPROVED
        );

        List<CommentDto> comments = Collections.emptyList();

        BookingDto bookingDto = BookingMapper.toBookingDto(booking, comments);

        assertNotNull(bookingDto);
        assertEquals(10L, bookingDto.getId());
        assertEquals(booking.getStart(), bookingDto.getStart());
        assertEquals(booking.getEnd(), bookingDto.getEnd());
        assertEquals(booking.getStatus(), bookingDto.getStatus());

        assertNotNull(bookingDto.getItem());
        assertEquals(item.getId(), bookingDto.getItem().getId());

        assertNotNull(bookingDto.getBooker());
        assertEquals(booker.getId(), bookingDto.getBooker().getId());
    }

    @Test
    void toBookingDto_FromBookingDtoPost_ShouldMapCorrectly() {
        BookingDtoPost post = new BookingDtoPost();
        post.setStart(LocalDateTime.of(2025, 6, 16, 12, 0));
        post.setEnd(LocalDateTime.of(2025, 6, 17, 12, 0));

        UserDto owner = new UserDto(3L, "owner", "owner@example.com");
        ItemDto item = new ItemDto(4L, "itemDtoName", "desc", 1L, 0L, true, List.of());

        BookingDto result = BookingMapper.toBookingDto(post, owner, item);

        assertNotNull(result);
        assertEquals(-1L, result.getId());
        assertEquals(post.getStart(), result.getStart());
        assertEquals(post.getEnd(), result.getEnd());
        assertEquals(owner, result.getBooker());
        assertEquals(item, result.getItem());
        assertEquals(BookingStatus.WAITING, result.getStatus());
    }

    @Test
    void toBooking_ShouldMapCorrectly() {
        UserDto userDto = new UserDto(2L, "user", "user@example.com");
        ItemDto itemDto = new ItemDto(1L, "item", "desc", 1L, 0L, true, List.of());

        BookingDto bookingDto = new BookingDto(
                5L,
                LocalDateTime.of(2025, 6, 16, 12, 0),
                LocalDateTime.of(2025, 6, 17, 12, 0),
                itemDto,
                userDto,
                BookingStatus.REJECTED
        );

        Booking booking = BookingMapper.toBooking(bookingDto);

        assertNotNull(booking);
        assertEquals(bookingDto.getId(), booking.getId());
        assertEquals(bookingDto.getStart(), booking.getStart());
        assertEquals(bookingDto.getEnd(), booking.getEnd());
        assertEquals(bookingDto.getStatus(), booking.getStatus());

        assertNotNull(booking.getItem());
        assertEquals(itemDto.getId(), booking.getItem().getId());

        assertNotNull(booking.getBooker());
        assertEquals(userDto.getId(), booking.getBooker().getId());
    }
}
