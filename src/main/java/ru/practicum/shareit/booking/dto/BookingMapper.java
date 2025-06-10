package ru.practicum.shareit.booking.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.List;

@UtilityClass
public class BookingMapper {

    public BookingDto toBookingDto(Booking booking, List<CommentDto> comments) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                ItemMapper.toItemDto(booking.getItem(), comments),
                UserMapper.toUserDto(booking.getBooker()),
                booking.getStatus()
        );
    }

    public BookingDto toBookingDto(BookingDtoPost bookingDtoPost, UserDto owner, ItemDto item) {
        return new BookingDto(
                -1L,
                bookingDtoPost.getStart(),
                bookingDtoPost.getEnd(),
                item,
                owner,
                BookingStatus.WAITING
        );
    }

    public Booking toBooking(BookingDto bookingDto) {
        return new Booking(
                bookingDto.getId(),
                bookingDto.getStart(),
                bookingDto.getEnd(),
                ItemMapper.toItem(bookingDto.getItem()),
                UserMapper.toUser(bookingDto.getBooker()),
                bookingDto.getStatus()
        );
    }
}
