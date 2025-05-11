package ru.practicum.shareit.booking.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserRepository;

@Component
public class BookingMapper {
    private static ItemRepository itemRepository;
    private static UserRepository userRepository;

    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd()
               /* booking.getItem().getId(),
                booking.getBooker().getId(),
                booking.getStatus()*/
        );
    }

    public static Booking toBooking(BookingDto bookingDto) {
        /*Item item = itemRepository.get(bookingDto.getItemId());
        User booker = userRepository.get(bookingDto.getBookerId());*/
        return new Booking(
                bookingDto.getId(),
                bookingDto.getStart(),
                bookingDto.getEnd()
               /* item,
                booker,
                bookingDto.getStatus()*/
        );
    }
}
