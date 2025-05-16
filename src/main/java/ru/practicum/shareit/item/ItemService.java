package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {
    ItemDto createItem(ItemDto itemDto, long userId);

    Collection<ItemDto> getAllItemsDto();

    ItemDto getItemDtoById(long itemDtoId);

    Collection<ItemDto> getAllItemsByOwnerId(long userId);

    Collection<ItemDto> getAllItemBySearch(String text);

    Collection<ItemDto> getAllItemsByOwnerIdAndSearch(long userId, String text);

    ItemDto updateItem(ItemDto itemDto, long ownerId, long idOfItem);

    ItemDto deleteItemById(long itemId, long ownerId);

    Collection<ItemDto> deleteAllItemsFromOwner(long userId);
}
