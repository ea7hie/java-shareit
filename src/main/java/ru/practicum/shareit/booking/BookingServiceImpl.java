package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.model.ValidationException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserRepository;

import java.util.Collection;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private BookingRepository bookingRepository;
    private ItemRepository itemRepository;
    private UserRepository userRepository;

    @Override
    public BookingDto createBooking(BookingDto bookingDto) {
        User booker = userRepository.getUserByID(bookingDto.getBookerId());
        Item item = itemRepository.getItemById(bookingDto.getItemId());
        Booking bookingForAdd = BookingMapper.toBooking(bookingDto, item, booker);

        if (isTimeOverlaps(bookingForAdd)) {
            throw new ValidationException("К сожалению, бронирование невозможно: товар уже забронирован на это время");
        }

        Booking booking = bookingRepository.createBooking(bookingForAdd);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public Collection<BookingDto> getAllBookings() {
        return bookingRepository.getAllBookings().stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }

    @Override
    public BookingDto getBookingById(long bookingId) {
        return BookingMapper.toBookingDto(bookingRepository.getBookingById(bookingId));
    }

    @Override
    public Collection<BookingDto> getAllBookingsByItemId(long itemId) {
        itemRepository.getItemById(itemId);
        return bookingRepository.getAllBookingsByItemId(itemId).stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }

    @Override
    public Collection<BookingDto> getAllBookingsByItemIdAndStatus(long itemId, BookingStatus bookingStatus) {
        itemRepository.getItemById(itemId);
        return bookingRepository.getAllBookingsByItemIdAndStatus(itemId, bookingStatus).stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }

    @Override
    public Collection<BookingDto> getAllBookingsByStatus(BookingStatus bookingStatus) {
        return bookingRepository.getAllBookingsByStatus(bookingStatus).stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }

    @Override
    public Collection<BookingDto> getAllBookingsFromBooker(long bookerId) {
        userRepository.getUserByID(bookerId);
        return bookingRepository.getAllBookingsFromBooker(bookerId).stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }

    @Override
    public Collection<BookingDto> getAllBookingsToOwner(long ownerId) {
        userRepository.getUserByID(ownerId);
        return bookingRepository.getAllBookingsToOwner(ownerId).stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }

    @Override
    public BookingDto updateBooking(BookingDto bookingDtoForUpdate, long userId) {
        userRepository.getUserByID(userId);
        return BookingMapper.toBookingDto(bookingRepository.updateBooking(bookingDtoForUpdate, userId));
    }

    @Override
    public BookingDto deleteBooking(long bookingDtoIdForDelete) {
        return BookingMapper.toBookingDto(bookingRepository.deleteBooking(bookingDtoIdForDelete));
    }

    private boolean isTimeOverlaps(Booking bookingForCheck) { //if true - то нельзя добавлять, есть пересечения!
        Collection<Booking> allApprovedBookingsByItemId = bookingRepository
                .getAllBookingsByItemIdAndStatus(bookingForCheck.getItem().getId(), BookingStatus.APPROVED);

        boolean isNotOverlap;
        for (Booking booking : allApprovedBookingsByItemId) {
            isNotOverlap = booking.getEnd().isBefore(booking.getStart())
                    || booking.getStart().isAfter(booking.getEnd());

            if (!isNotOverlap) {
                return true;
            }
        }

        return false;
    }
}
