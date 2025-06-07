package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.enums.Actions;
import ru.practicum.shareit.exception.model.AccessError;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.exception.model.ValidationException;
import ru.practicum.shareit.item.comment.*;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForOwner;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    private final String messageCantCommented = "Оставлять отзывы могут только пользователи, которые пользовались им.";
    private final String messageCantDelete = "Удалять данные о вещи может только владелец.";
    private final String messageCantUpdate = "Редактировать данные о вещи может только владелец.";

    @Override
    @Transactional
    public ItemDto createItem(ItemDto itemDto, long userId) {
        isUserExistsById(userId);
        itemDto.setOwnerId(userId);
        Item itemForSave = itemRepository.save(ItemMapper.toItem(itemDto));
        return ItemMapper.toItemDto(itemForSave, getCommentDtosByItemIdForItemDto(itemForSave.getId()));
    }

    @Override
    public ItemDtoForOwner getItemDtoById(long itemDtoId, long ownerId) {
        Item item = getItemOrThrow(itemDtoId, Actions.TO_VIEW);
        return getItemDtoForOwnerFromItem(item, ownerId);
    }

    @Override
    public Collection<ItemDtoForOwner> getAllItemsByOwnerId(long userId) {
        isUserExistsById(userId);

        Collection<Item> allItemsByOwnerId = itemRepository.findAllByOwnerId(userId);

        return allItemsByOwnerId.stream()
                .map(item -> getItemDtoForOwnerFromItem(item, userId))
                .toList();
    }

    @Override
    public Collection<ItemDto> getAllItemBySearch(String text) {
        if (text.isEmpty()) {
            return List.of();
        }

        return itemRepository
                .findByDescriptionContainsIgnoreCaseAndIsAvailableIsTrueOrNameContainsIgnoreCaseAndIsAvailableIsTrue(
                        text, text)
                .stream()
                .map(item -> ItemMapper.toItemDto(item, getCommentDtosByItemIdForItemDto(item.getId())))
                .toList();
    }

    @Override
    @Transactional
    public ItemDto updateItem(ItemDto itemDto, long ownerId, long idOfItem) {
        isUserExistsById(ownerId);

        Item itemForUpdate = getItemOrThrow(idOfItem, Actions.TO_UPDATE);

        if (itemForUpdate.getOwnerId() != ownerId) {
            throw new AccessError(messageCantUpdate);
        }

        itemForUpdate.setName(itemDto.getName() == null ? itemForUpdate.getName() : itemDto.getName());
        itemForUpdate.setDescription(itemDto.getDescription() == null ? itemForUpdate.getDescription()
                : itemDto.getDescription());
        itemForUpdate.setAvailable(itemDto.getAvailable() == null ? itemForUpdate.isAvailable()
                : itemDto.getAvailable());

        itemRepository.updateItem(idOfItem, itemForUpdate.getName(), itemForUpdate.getDescription(),
                itemForUpdate.isAvailable());

        return ItemMapper.toItemDto(itemForUpdate, getCommentDtosByItemIdForItemDto(idOfItem));
    }

    @Override
    @Transactional
    public ItemDto deleteItemById(long itemId, long ownerId) {
        isUserExistsById(ownerId);

        Item itemForDelete = getItemOrThrow(itemId, Actions.TO_DELETE);
        if (itemForDelete.getOwnerId() == ownerId) {
            itemRepository.deleteById(itemId);
            return ItemMapper.toItemDto(itemForDelete, getCommentDtosByItemIdForItemDto(itemId));
        }
        throw new AccessError(messageCantDelete);
    }

    @Override
    @Transactional
    public void deleteAllItemsFromOwner(long userId) {
        isUserExistsById(userId);
        itemRepository.deleteAllItemsFromOwner(userId);
    }

    @Override
    @Transactional
    public CommentDto createNewComment(CommentDtoForCreate commentDtoForCreate, long authorId, long itemId) {
        Item item = getItemOrThrow(itemId, Actions.TO_VIEW);
        User author = getUserOrThrow(authorId, Actions.TO_VIEW);

        boolean hasBooking = bookingRepository.existsByItemIdAndBookerIdAndStatusAndEndBefore(itemId, authorId,
                BookingStatus.APPROVED, LocalDateTime.now());
        if (hasBooking) {
            Comment comment = commentRepository.save(new Comment(-1L, commentDtoForCreate.getText(),
                    author, item, LocalDateTime.now()));
            return CommentMapper.toCommentDto(comment);
        }

        throw new ValidationException(messageCantCommented);
    }

    public List<CommentDto> getCommentDtosByItemIdForItemDto(long itemId) {
        return getCommentsByItemId(itemId).stream()
                .map(CommentMapper::toCommentDto)
                .toList();
    }

    private ItemDtoForOwner getItemDtoForOwnerFromItem(Item item, long ownerId) {
        if (item.getOwnerId() != ownerId) {
            return ItemMapper.toItemDtoForOwner(item, null, null,
                    getCommentDtosByItemIdForItemDto(item.getId()));
        }

        LocalDateTime now = LocalDateTime.now();
        Optional<Booking> optionalLastBooking = bookingRepository
                .findFirstOneByItemIdAndStatusAndEndBeforeOrderByEndDesc(
                        item.getId(), BookingStatus.APPROVED, now);

        Optional<Booking> optionalNextBooking = bookingRepository
                .findFirstOneByItemIdAndStatusAndStartAfterOrderByStartAsc(
                        item.getId(), BookingStatus.APPROVED, now);

        BookingDto lastBookingDto = optionalLastBooking
                .map(booking -> BookingMapper.toBookingDto(booking, getCommentDtosByItemIdForItemDto(item.getId())))
                .orElse(null);


        BookingDto nextBookingDto = optionalNextBooking
                .map(booking -> BookingMapper.toBookingDto(booking, getCommentDtosByItemIdForItemDto(item.getId())))
                .orElse(null);

        return ItemMapper.toItemDtoForOwner(item, lastBookingDto, nextBookingDto,
                getCommentDtosByItemIdForItemDto(item.getId()));
    }

    private List<Comment> getCommentsByItemId(long itemId) {
        return (List<Comment>) commentRepository.findAllByItemId(itemId);
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