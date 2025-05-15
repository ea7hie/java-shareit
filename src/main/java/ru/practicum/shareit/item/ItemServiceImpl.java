package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.model.AccessError;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserRepository;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    private final String messageCantDelete = "Удалять данные о вещи может только владелец.";
    private final String messageCantUpdate = "Редактировать данные о вещи может только владелец.";

    @Override
    public ItemDto createItem(ItemDto itemDto, long userId) {
        User owner = userRepository.getUserByID(userId);
        return ItemMapper.toItemDto(itemRepository.createItem(ItemMapper.toItem(itemDto, owner)));
    }

    @Override
    public Collection<ItemDto> getAllItemsDto() {
        return itemRepository.getAllItems().stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public ItemDto getItemDtoById(long itemDtoId) {
        return ItemMapper.toItemDto(itemRepository.getItemById(itemDtoId));
    }

    @Override
    public Collection<ItemDto> getAllItemsByOwnerId(long userId) {
        userRepository.getUserByID(userId);

        return itemRepository.getAllItemsByOwnerId(userId).stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public Collection<ItemDto> getAllItemBySearch(String text) {
        if (text.isEmpty()) {
            return List.of();
        }

        return itemRepository.getAllItemBySearch(text).stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public Collection<ItemDto> getAllItemsByOwnerIdAndSearch(long userId, String text) {
        if (text.isEmpty()) {
            return List.of();
        }

        userRepository.getUserByID(userId);

        return itemRepository.getAllItemsByOwnerIdAndSearch(userId, text).stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, long ownerId) {
        userRepository.getUserByID(ownerId);
        Item itemById = itemRepository.getItemById(itemDto.getId());
        if (itemById.getOwner().getId() == ownerId) {
            return ItemMapper.toItemDto(itemRepository.updateItem(itemDto));
        }
        throw new AccessError(messageCantUpdate);
    }

    @Override
    public ItemDto deleteItemById(long itemId, long ownerId) {
        userRepository.getUserByID(ownerId);
        if (itemRepository.getItemById(itemId).getOwner().getId() == ownerId) {
            return ItemMapper.toItemDto(itemRepository.deleteItemById(itemId));
        }
        throw new AccessError(messageCantDelete);
    }

    @Override
    public Collection<ItemDto> deleteAllItemsFromOwner(long userId) {
        userRepository.getUserByID(userId);
        return itemRepository.deleteAllItemsFromOwner(userId).stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }
}