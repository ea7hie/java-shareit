package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoPost;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.enums.Actions;
import ru.practicum.shareit.enums.BookingDtoStates;
import ru.practicum.shareit.exception.model.AccessError;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.exception.model.ValidationException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserMapper;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    private final String messageIsOverlaps =
            "К сожалению, бронирование невозможно: товар уже забронирован на это время.";
    private final String messageCantView = "У вас нет прав доступа к просмотру этой брони.";
    private final String messageCantUpdate = "У вас нет прав доступа к редактированию этой брони.";
    private final String messageCantDelete = "У вас нет прав доступа к удалению этой брони.";

    @Override
    @Transactional
    public BookingDto createBooking(BookingDtoPost bookingDtoPost, long bookerId) {
        Item item = getItemOrThrow(bookingDtoPost.getItemId(), Actions.TO_VIEW);
        User booker = getUserOrThrow(bookerId, Actions.TO_VIEW);

        if (!item.isAvailable()) {
            throw new ValidationException(messageIsOverlaps);
        }

        ItemDto itemDto = ItemMapper.toItemDto(item, getCommentDtosByItemId(item.getId()));
        BookingDto bookingDto = BookingMapper.toBookingDto(bookingDtoPost, UserMapper.toUserDto(booker), itemDto);
        Booking bookingForAdd = BookingMapper.toBooking(bookingDto);

        if (isTimeOverlaps(bookingForAdd)) {
            throw new ValidationException(messageIsOverlaps);
        }

        return BookingMapper.toBookingDto(bookingRepository.save(bookingForAdd), itemDto.getComments());
    }

    @Override
    public BookingDto getBookingById(long bookingId, long userId) {
        isUserExistsById(userId);
        Booking booking = getBookingOrThrow(bookingRepository, bookingId, Actions.TO_VIEW);
        Item item = getItemOrThrow(booking.getItem().getId(), Actions.TO_VIEW);

        if (booking.getBooker().getId() == userId || item.getOwnerId() == userId) {
            return BookingMapper.toBookingDto(booking, getCommentDtosByItemId(item.getId()));
        }

        throw new AccessError(messageCantView);
    }

    @Override
    public Collection<BookingDto> getAllBookingsByItemId(long itemId) { //всего 1 вещь, выгружаем все отзывы к ней сразу
        List<CommentDto> commentDtosByItemId = getCommentDtosByItemId(itemId);
        return bookingRepository.findAllByItemId(itemId).stream()
                .map(booking -> BookingMapper.toBookingDto(booking, commentDtosByItemId))
                .toList();
    }

    @Override
    public Collection<BookingDto> getAllBookingsByItemIdAndStatus(long itemId, BookingStatus bookingStatus) {
        if (!itemRepository.existsById(itemId)) {
            throw new NotFoundException(String.format("Вещи с id = %d для %s не найдено", itemId, Actions.TO_VIEW));
        }

        if (bookingStatus == null) {
            return getAllBookingsByItemId(itemId);
        }

        //всего 1 вещь, выгружаем все отзывы к ней сразу
        List<CommentDto> commentDtosByItemId = getCommentDtosByItemId(itemId);
        return bookingRepository.findAllByItemIdAndStatus(itemId, bookingStatus).stream()
                .map(booking -> BookingMapper.toBookingDto(booking, commentDtosByItemId))
                .toList();
    }

    @Override
    public Collection<BookingDto> getAllBookingsByStatusForOwnerInAmount(long userId, BookingDtoStates state,
                                                                         int from, int size) {
        isUserExistsById(userId);
        return bookingRepository.findAllByBookerId(userId).stream()
                .skip(from)
                .limit(size)
                .map(booking -> BookingMapper.toBookingDto(booking, getCommentDtosByItemId(booking.getItem().getId())))
                .toList();
    }

    @Override
    public Collection<BookingDto> getAllBookingsByStatus(long userId, BookingDtoStates state) {
        isUserExistsById(userId);

        LocalDateTime now = LocalDateTime.now();
        Collection<Booking> foundedBookings = switch (state) {
            case ALL -> bookingRepository.findAllByBookerId(userId);
            case CURRENT -> bookingRepository.findByBookerIdAndStartBeforeAndEndAfterOrderByStartAsc(userId, now, now);
            case PAST -> bookingRepository.findAllByBookerIdAndEndBeforeOrderByEndDesc(userId, now);
            case FUTURE -> bookingRepository.findAllByBookerIdAndStartAfterOrderByStartAsc(userId, now);
            case WAITING -> bookingRepository.findAllByBookerIdAndStatus(userId, BookingStatus.WAITING);
            case REJECTED -> bookingRepository.findAllByBookerIdAndStatus(userId, BookingStatus.REJECTED);
        };

        Set<Long> allItemsIds = foundedBookings.stream()
                .map(booking -> booking.getItem().getId())
                .collect(Collectors.toSet());
        Collection<Comment> commentsByItemIds = getCommentsByItemIds(allItemsIds);

        return foundedBookings.stream()
                .map(booking -> BookingMapper.toBookingDto(booking,
                        getCommentDtosForOneItemDtoFromComments(commentsByItemIds, booking.getItem().getId())))
                .toList();
    }

    @Override
    public Collection<BookingDto> getAllBookingsFromBooker(long bookerId) {
        isUserExistsById(bookerId);

        Collection<Booking> foundedBookings = bookingRepository.findAllByBookerId(bookerId);
        Set<Long> allItemsIds = foundedBookings.stream()
                .map(booking -> booking.getItem().getId())
                .collect(Collectors.toSet());
        Collection<Comment> commentsByItemIds = getCommentsByItemIds(allItemsIds);

        return foundedBookings.stream()
                .map(booking -> BookingMapper.toBookingDto(booking,
                        getCommentDtosForOneItemDtoFromComments(commentsByItemIds, booking.getItem().getId())))
                .toList();
    }

    @Override
    public Collection<BookingDto> getAllBookingsByStatusForOwner(long ownerId, BookingDtoStates state) {
        isUserExistsById(ownerId);

        LocalDateTime now = LocalDateTime.now();
        Collection<Booking> foundedBookings = switch (state) {
            case ALL -> bookingRepository.findAllByItemOwnerId(ownerId);
            case CURRENT -> bookingRepository.findByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartAsc(
                    ownerId, now, now);
            case PAST -> bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByEndDesc(ownerId, now);
            case FUTURE -> bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartAsc(ownerId, now);
            case WAITING -> bookingRepository.findAllByItemOwnerIdAndStatus(ownerId, BookingStatus.WAITING);
            case REJECTED -> bookingRepository.findAllByItemOwnerIdAndStatus(ownerId, BookingStatus.REJECTED);
        };

        Set<Long> allItemsIds = foundedBookings.stream()
                .map(booking -> booking.getItem().getId())
                .collect(Collectors.toSet());
        Collection<Comment> commentsByItemIds = getCommentsByItemIds(allItemsIds);

        return foundedBookings.stream()
                .map(booking -> BookingMapper.toBookingDto(booking,
                        getCommentDtosForOneItemDtoFromComments(commentsByItemIds, booking.getItem().getId())))
                .toList();
    }

    @Override
    @Transactional
    public BookingDto updateBooking(long bookingId, long userId, boolean approved) {
        Booking bookingForUpdate = getBookingOrThrow(bookingRepository, bookingId, Actions.TO_UPDATE);
        Item item = getItemOrThrow(bookingForUpdate.getItem().getId(), Actions.TO_UPDATE);

        if (item.getOwnerId() == userId) {
            bookingForUpdate.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        } else {
            throw new AccessError(messageCantUpdate);
        }

        bookingRepository.updateBooking(bookingId, bookingForUpdate.getStatus());
        return BookingMapper.toBookingDto(bookingForUpdate, getCommentDtosByItemId(item.getId()));
    }

    @Override
    @Transactional
    public BookingDto deleteBooking(long bookingDtoIdForDelete, long userId) {
        isUserExistsById(userId);
        Booking bookingForDelete = getBookingOrThrow(bookingRepository, bookingDtoIdForDelete, Actions.TO_DELETE);

        if (bookingForDelete.getBooker().getId() != userId) {
            throw new AccessError(messageCantDelete);
        }

        bookingRepository.deleteById(bookingDtoIdForDelete);

        return BookingMapper.toBookingDto(bookingForDelete, getCommentDtosByItemId(bookingForDelete.getItem().getId()));
    }

    public boolean isTimeOverlaps(Booking bookingForCheck) { //if true - то нельзя добавлять, есть пересечения!
        Collection<Booking> allApprovedBookingsByItemId = bookingRepository
                .findAllByItemIdAndStatus(bookingForCheck.getItem().getId(), BookingStatus.APPROVED);

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

    private List<CommentDto> getCommentDtosByItemId(long itemId) {
        return commentRepository.findAllByItemId(itemId).stream()
                .map(CommentMapper::toCommentDto)
                .toList();
    }

    private Collection<Comment> getCommentsByItemIds(Collection<Long> itemIds) {
        return commentRepository.findAllByItemIdIn(itemIds);
    }

    private List<CommentDto> getCommentDtosForOneItemDtoFromComments(Collection<Comment> allComments, long itemID) {
        return allComments.stream()
                .filter(comment -> comment.getItem().getId() == itemID)
                .map(CommentMapper::toCommentDto)
                .toList();
    }

    private Booking getBookingOrThrow(BookingRepository bookingRepository, long bookingId, String message) {
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isEmpty()) {
            throw new NotFoundException(String.format("Бронирования с id = %d для %s не найдено", bookingId, message));
        }
        return optionalBooking.get();
    }

    private Item getItemOrThrow(long itemId, String message) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isEmpty()) {
            throw new NotFoundException(String.format("Вещи с id = %d для %s не найдено", itemId, message));
        }
        return optionalItem.get();
    }

    private void isUserExistsById(long userIdForCheck) {
        Optional<User> optionalUser = userRepository.findById(userIdForCheck);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException(String.format("Пользователя с id = %d для %s не найдено", userIdForCheck,
                    Actions.TO_VIEW));
        }
    }

    private User getUserOrThrow(long userId, String message) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException(String.format("Пользователя с id = %d для %s не найдено", userId, message));
        }
        return optionalUser.get();
    }
}
