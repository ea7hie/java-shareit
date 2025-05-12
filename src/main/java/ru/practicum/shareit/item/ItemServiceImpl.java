package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.model.AccessError;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserRepository;

import java.util.Collection;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private ItemRepository itemRepository;
    private UserRepository userRepository;

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
        return itemRepository.getAllItemBySearch(text).stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public Collection<ItemDto> getAllItemsByOwnerIdAndSearch(long userId, String text) {
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
        throw new AccessError("Редактировать данные о вещи может только владелец");
    }

    @Override
    public ItemDto deleteItemById(long itemId, long ownerId) {
        userRepository.getUserByID(ownerId);
        if (itemRepository.getItemById(itemId).getOwner().getId() == ownerId) {
            return ItemMapper.toItemDto(itemRepository.deleteItemById(itemId));
        }
        throw new AccessError("Удалять данные о вещи может только владелец");
    }

    @Override
    public Collection<ItemDto> deleteAllItemsFromOwner(long userId) {
        userRepository.getUserByID(userId);
        return itemRepository.deleteAllItemsFromOwner(userId).stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }
}