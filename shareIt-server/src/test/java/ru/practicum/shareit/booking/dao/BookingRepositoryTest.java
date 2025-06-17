package ru.practicum.shareit.booking.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TestEntityManager em;

    private Booking createBooking(User owner, User booker, BookingStatus status, LocalDateTime start, LocalDateTime end) {
        Item item = new Item(null, "Drill", "Drill description", owner.getId(), 0L, true);
        em.persist(item);
        Booking booking = new Booking(null, start, end, item, booker, status);
        em.persist(booking);
        em.flush();
        return booking;
    }

    private User createUser(String name, String email) {
        User user = new User(null, name, email);
        em.persist(user);
        return user;
    }

    @Test
    @DisplayName("Должен находить бронирования по itemId")
    void findAllByItemId_shouldReturnBookings() {
        User owner = new User(null, "Owner", "owner@example.com");
        User booker = new User(null, "Booker", "booker@example.com");

        em.persist(owner);
        em.persist(booker);

        Item item = new Item(null, "Drill", "Electric drill", owner.getId(), 0L, true);
        em.persist(item);

        Booking booking = new Booking(null,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                item,
                booker,
                BookingStatus.WAITING);

        em.persist(booking);
        em.flush();

        List<Booking> found = (List<Booking>) bookingRepository.findAllByItemId(item.getId());

        assertThat(found).hasSize(1);
        assertThat(found.getFirst().getItem().getId()).isEqualTo(item.getId());
    }

    @Test
    @DisplayName("Поиск по itemId и статусу")
    void findAllByItemIdAndStatus() {
        User owner = createUser("Owner", "owner@mail.com");
        User booker = createUser("Booker", "booker@mail.com");

        Booking booking = createBooking(owner, booker, BookingStatus.APPROVED,
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));

        List<Booking> result = (List<Booking>) bookingRepository.findAllByItemIdAndStatus(
                booking.getItem().getId(), BookingStatus.APPROVED);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo(BookingStatus.APPROVED);
    }

    @Test
    @DisplayName("Поиск будущих бронирований по bookerId")
    void findAllByBookerIdAndStartAfterOrderByStartAsc() {
        User owner = createUser("Owner", "owner@mail.com");
        User booker = createUser("Booker", "booker@mail.com");

        createBooking(owner, booker, BookingStatus.WAITING,
                LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(4));

        List<Booking> result = (List<Booking>) bookingRepository
                .findAllByBookerIdAndStartAfterOrderByStartAsc(booker.getId(), LocalDateTime.now());

        assertThat(result).hasSize(1);
    }

    /*@Test
    @DisplayName("Поиск прошлых бронирований по bookerId")
    void findAllByBookerIdAndEndBeforeOrderByEndDesc() {
        User owner = createUser("Owner", "owner@mail.com");
        User booker = createUser("Booker", "booker@mail.com");

        createBooking(owner, booker, BookingStatus.APPROVED,
                LocalDateTime.now().minusDays(3), LocalDateTime.now().minusDays(1));

        List<Booking> result = (List<Booking>) bookingRepository
                .findAllByBookerIdAndEndBeforeOrderByEndDesc(booker.getId(), LocalDateTime.now());

        assertThat(result).hasSize(1);
    }*/

    /*@Test
    @DisplayName("Поиск активного бронирования по bookerId (между start и end)")
    void findByBookerIdAndStartBeforeAndEndAfterOrderByStartAsc() {
        User owner = createUser("Owner", "owner@mail.com");
        User booker = createUser("Booker", "booker@mail.com");

        createBooking(owner, booker, BookingStatus.APPROVED,
                LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(1));

        List<Booking> result = (List<Booking>) bookingRepository
                .findByBookerIdAndStartBeforeAndEndAfterOrderByStartAsc(booker.getId(), LocalDateTime.now(), LocalDateTime.now());

        assertThat(result).hasSize(1);
    }*/

    /*@Test
    @DisplayName("Поиск первого завершённого бронирования для item")
    void findFirstOneByItemIdAndStatusAndEndBeforeOrderByEndDesc() {
        User owner = createUser("Owner", "owner@mail.com");
        User booker = createUser("Booker", "booker@mail.com");

        Booking pastBooking = createBooking(owner, booker, BookingStatus.APPROVED,
                LocalDateTime.now().minusDays(5), LocalDateTime.now().minusDays(3));

        Optional<Booking> result = bookingRepository
                .findFirstOneByItemIdAndStatusAndEndBeforeOrderByEndDesc(pastBooking.getItem().getId(),
                        BookingStatus.APPROVED, LocalDateTime.now());

        assertThat(result).isPresent();
    }*/

   /* @Test
    @DisplayName("Проверка existsBy для уже завершённого бронирования")
    void existsByItemIdAndBookerIdAndStatusAndEndBefore() {
        User owner = createUser("Owner", "owner@mail.com");
        User booker = createUser("Booker", "booker@mail.com");

        Booking booking = createBooking(owner, booker, BookingStatus.APPROVED,
                LocalDateTime.now().minusDays(4), LocalDateTime.now().minusDays(2));

        boolean exists = bookingRepository.existsByItemIdAndBookerIdAndStatusAndEndBefore(
                booking.getItem().getId(), booker.getId(), BookingStatus.APPROVED, LocalDateTime.now());

        assertThat(exists).isTrue();
    }*/

    @Test
    @DisplayName("Поиск по ownerId и статусу")
    void findAllByItemOwnerIdAndStatus() {
        User owner = createUser("Owner", "owner@mail.com");
        User booker = createUser("Booker", "booker@mail.com");

        Booking booking = createBooking(owner, booker, BookingStatus.REJECTED,
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));

        List<Booking> result = (List<Booking>) bookingRepository
                .findAllByItemOwnerIdAndStatus(owner.getId(), BookingStatus.REJECTED);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo(BookingStatus.REJECTED);
    }

    @Test
    @DisplayName("Поиск будущих бронирований по ownerId")
    void findAllByItemOwnerIdAndStartAfterOrderByStartAsc() {
        User owner = createUser("Owner", "owner@mail.com");
        User booker = createUser("Booker", "booker@mail.com");

        createBooking(owner, booker, BookingStatus.WAITING,
                LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(3));

        List<Booking> result = (List<Booking>) bookingRepository
                .findAllByItemOwnerIdAndStartAfterOrderByStartAsc(owner.getId(), LocalDateTime.now());

        assertThat(result).hasSize(1);
    }

    /*@Test
    @DisplayName("Поиск текущих бронирований по ownerId")
    void findByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartAsc() {
        User owner = createUser("Owner", "owner@mail.com");
        User booker = createUser("Booker", "booker@mail.com");

        createBooking(owner, booker, BookingStatus.APPROVED,
                LocalDateTime.now().minusHours(2), LocalDateTime.now().plusHours(2));

        List<Booking> result = (List<Booking>) bookingRepository
                .findByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartAsc(owner.getId(), LocalDateTime.now(), LocalDateTime.now());

        assertThat(result).hasSize(1);
    }*/

    @Test
    @DisplayName("Поиск ближайшего будущего бронирования для itemId")
    void findFirstOneByItemIdAndStatusAndStartAfterOrderByStartAsc() {
        User owner = createUser("Owner", "owner@mail.com");
        User booker = createUser("Booker", "booker@mail.com");

        Booking booking = createBooking(owner, booker, BookingStatus.APPROVED,
                LocalDateTime.now().plusHours(5), LocalDateTime.now().plusHours(8));

        Optional<Booking> result = bookingRepository
                .findFirstOneByItemIdAndStatusAndStartAfterOrderByStartAsc(
                        booking.getItem().getId(), BookingStatus.APPROVED, LocalDateTime.now());

        assertThat(result).isPresent();
        assertThat(result.get().getStart()).isAfter(LocalDateTime.now());
    }

    @Test
    @DisplayName("Обновление статуса бронирования")
    void updateBookingStatus() {
        User owner = createUser("Owner", "owner@mail.com");
        User booker = createUser("Booker", "booker@mail.com");

        Booking booking = createBooking(owner, booker, BookingStatus.WAITING,
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));

        bookingRepository.updateBooking(booking.getId(), BookingStatus.APPROVED);
        em.flush(); // важно, чтобы обновление дошло до БД
        em.clear(); // сбрасываем кэш

        Booking updated = em.find(Booking.class, booking.getId());
        assertThat(updated.getStatus()).isEqualTo(BookingStatus.APPROVED);
    }
}
