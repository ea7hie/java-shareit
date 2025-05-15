package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    private final String messageIsOverlaps =
            "К сожалению, бронирование невозможно: товар уже забронирован на это время.";

    @Override
    public BookingDto createBooking(BookingDto bookingDto) {
        User booker = userRepository.getUserByID(bookingDto.getBookerId());
        Item item = itemRepository.getItemById(bookingDto.getItemId());
        Booking bookingForAdd = BookingMapper.toBooking(bookingDto, item, booker);

        if (isTimeOverlaps(bookingForAdd)) {
            throw new ValidationException(messageIsOverlaps);
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
        return bookingRepository.getAllBookingsByItemId(itemId).stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }

    @Override
    public Collection<BookingDto> getAllBookingsByItemIdAndStatus(long itemId, BookingStatus bookingStatus) {
        itemRepository.getItemById(itemId);
        
        if (bookingStatus == null) {
            return getAllBookingsByItemId(itemId);
        }

        return bookingRepository.getAllBookingsByItemIdAndStatus(itemId, bookingStatus).stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }

    @Override
    public Collection<BookingDto> getAllBookingsByStatus(BookingStatus bookingStatus) {
        if (bookingStatus == null) {
            return getAllBookings();
        }

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
    public BookingDto deleteBooking(long bookingDtoIdForDelete, long userId) {
        userRepository.getUserByID(userId);
        return BookingMapper.toBookingDto(bookingRepository.deleteBooking(bookingDtoIdForDelete, userId));
    }

    @Override
    public boolean isTimeOverlaps(Booking bookingForCheck) { //if true - то нельзя добавлять, есть пересечения!
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
