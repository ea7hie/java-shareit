package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements  BookingService{
    private BookingRepository bookingRepository;
    private BookingMapper mapper;

//    @Override
//    public Booking createBooking(Booking booking) {
//        return bookingRepository.createBooking(booking);
//    }
//
//    @Override
//    public Booking getBooking(long bookingIg) {
//        return bookingRepository.getBooking(bookingIg);
//    }
//
//    @Override
//    public Booking updateBooking(Booking bookingForUpdate) {
//        return bookingRepository.updateBooking(bookingForUpdate);
//    }
//
//    @Override
//    public Booking deleteBooking(long bookingIdForDelete) {
//        return bookingRepository.deleteBooking(bookingIdForDelete);
//    }

    @Override
    public BookingDto createBooking(BookingDto bookingDto) {
        Booking booking = bookingRepository.createBooking(BookingMapper.toBooking(bookingDto));
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto getBooking(long bookingDtoId) {
        return null;
    }

    @Override
    public BookingDto updateBooking(BookingDto bookingDtoForUpdate) {
        return null;
    }

    @Override
    public BookingDto deleteBooking(long bookingDtoIdForDelete) {
        return null;
    }
}
