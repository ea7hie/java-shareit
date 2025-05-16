package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository {
    Item createItem(Item item);

    Collection<Item> getAllItems();

    Item getItemById(long itemId);

    Collection<Item> getAllItemsByOwnerId(long userId);

    Collection<Item> getAllItemBySearch(String text);

    Collection<Item> getAllItemsByOwnerIdAndSearch(long userId, String text);

    Item updateItem(ItemDto itemDto);

    Item deleteItemById(long itemId);

    Collection<Item> deleteAllItemsFromOwner(long userId);
}
