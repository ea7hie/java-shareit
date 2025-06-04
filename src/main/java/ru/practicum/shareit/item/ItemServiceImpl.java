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
import ru.practicum.shareit.exception.model.ValidationException;
import ru.practicum.shareit.item.comment.*;
import ru.practicum.shareit.item.dao.ItemChecks;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForOwner;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserChecks;
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
        UserChecks.isUserExistsById(userRepository, userId);
        itemDto.setOwnerId(userId);
        Item itemForSave = itemRepository.save(ItemMapper.toItem(itemDto));
        return ItemMapper.toItemDto(itemForSave, getCommentsByItemId(itemForSave.getId()));
    }

    @Override
    public ItemDtoForOwner getItemDtoById(long itemDtoId, long ownerId) {
        Item item = ItemChecks.getItemOrThrow(itemRepository, itemDtoId, Actions.TO_VIEW);
        ItemDtoForOwner itemDtoForOwner = getItemDtoForOwnerFromItem(item);
        if (item.getOwnerId() != ownerId) {
            itemDtoForOwner.setLastBooking(null);
            itemDtoForOwner.setNextBooking(null);
        }
        return itemDtoForOwner;
    }

    @Override
    public Collection<ItemDtoForOwner> getAllItemsByOwnerId(long userId) {
        UserChecks.isUserExistsById(userRepository, userId);

        Collection<Item> allItemsByOwnerId = itemRepository.findAllByOwnerId(userId);

        return allItemsByOwnerId.stream()
                .map(this::getItemDtoForOwnerFromItem)
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
                .map(item -> ItemMapper.toItemDto(item, getCommentsByItemId(item.getId())))
                .toList();
    }

    @Override
    @Transactional
    public ItemDto updateItem(ItemDto itemDto, long ownerId, long idOfItem) {
        UserChecks.isUserExistsById(userRepository, ownerId);

        Item itemForUpdate = ItemChecks.getItemOrThrow(itemRepository, idOfItem, Actions.TO_UPDATE);

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

        return ItemMapper.toItemDto(itemForUpdate, getCommentsByItemId(idOfItem));
    }

    @Override
    @Transactional
    public ItemDto deleteItemById(long itemId, long ownerId) {
        UserChecks.isUserExistsById(userRepository, ownerId);

        Item itemForDelete = ItemChecks.getItemOrThrow(itemRepository, itemId, Actions.TO_DELETE);
        if (itemForDelete.getOwnerId() == ownerId) {
            itemRepository.deleteById(itemId);
            return ItemMapper.toItemDto(itemForDelete, getCommentsByItemId(itemId));
        }
        throw new AccessError(messageCantDelete);
    }

    @Override
    @Transactional
    public Collection<ItemDto> deleteAllItemsFromOwner(long userId) {
        UserChecks.isUserExistsById(userRepository, userId);

        Collection<Item> allByOwnerId = itemRepository.findAllByOwnerId(userId);

        itemRepository.deleteAllItemsFromOwner(userId);

        return allByOwnerId.stream()
                .map(item -> ItemMapper.toItemDto(item, getCommentsByItemId(item.getId())))
                .toList();
    }

    @Override
    @Transactional
    public CommentDto createNewComment(CommentDtoForCreate commentDtoForCreate, long authorId, long itemId) {
        Item item = ItemChecks.getItemOrThrow(itemRepository, itemId, Actions.TO_VIEW);
        User author = UserChecks.getUserOrThrow(userRepository, authorId, Actions.TO_VIEW);

        boolean hasBooking = bookingRepository.existsByItemIdAndBookerIdAndStatusAndEndBefore(itemId, authorId,
                BookingStatus.APPROVED, LocalDateTime.now());
        if (hasBooking) {
            Comment comment = commentRepository.save(new Comment(-1L, commentDtoForCreate.getText(),
                    author, item, LocalDateTime.now()));
            return CommentMapper.toCommentDto(comment);
        }

        throw new ValidationException(messageCantCommented);
    }

    private ItemDtoForOwner getItemDtoForOwnerFromItem(Item item) {
        LocalDateTime now = LocalDateTime.now();
        Optional<Booking> optionalLastBooking = bookingRepository
                .findFirstOneByItemIdAndStatusAndEndBeforeOrderByEndDesc(
                item.getId(), BookingStatus.APPROVED, now);

        Optional<Booking> optionalNextBooking = bookingRepository
                .findFirstOneByItemIdAndStatusAndStartAfterOrderByStartAsc(
                item.getId(), BookingStatus.APPROVED, now);

        BookingDto lastBookingDto = optionalLastBooking.map(BookingMapper::toBookingDto).orElse(null);
        BookingDto nextBookingDto = optionalNextBooking.map(BookingMapper::toBookingDto).orElse(null);

        return ItemMapper.toItemDtoForOwner(item, lastBookingDto, nextBookingDto, getCommentsByItemId(item.getId()));
    }

    private List<CommentDto> getCommentsByItemId(long itemId) {
        return commentRepository.findAllByItemId(itemId).stream()
                .map(CommentMapper::toCommentDto)
                .toList();
    }
}