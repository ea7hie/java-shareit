package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.enums.Actions;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, Item> allItemsById = new HashMap<>();
    private long id = 0;

    @Override
    public Item createItem(Item item) {
        item.setId(getNewId());
        allItemsById.put(item.getId(), item);
        return item;
    }

    @Override
    public Collection<Item> getAllItems() {
        return allItemsById.values();
    }

    @Override
    public Item getItemById(long itemId) {
        return getItemOrThrow(itemId, Actions.TO_VIEW);
    }

    @Override
    public Collection<Item> getAllItemsByOwnerId(long userId) {
        return allItemsById.values().stream()
                .filter(item -> item.getOwner().getId() == userId)
                .toList();
    }

    @Override
    public Collection<Item> getAllItemBySearch(String text) {
        return allItemsById.values().stream()
                .filter(item -> this.hasTextInDesc(item, text))
                .filter(Item::isAvailable)
                .toList();
    }

    @Override
    public Collection<Item> getAllItemsByOwnerIdAndSearch(long userId, String text) {
        return allItemsById.values().stream()
                .filter(item -> item.getOwner().getId() == userId)
                .filter(item -> this.hasTextInDesc(item, text))
                .filter(Item::isAvailable)
                .toList();
    }

    @Override
    public Item updateItem(ItemDto itemDto) {
        Item itemForUpdate = getItemOrThrow(itemDto.getId(), Actions.TO_UPDATE);

        itemForUpdate.setName(itemDto.getName() == null ? itemForUpdate.getName() : itemDto.getName());
        itemForUpdate.setDescription(itemDto.getDescription() == null ? itemForUpdate.getDescription()
                : itemDto.getDescription());
        itemForUpdate.setAvailable(itemDto.getAvailable() == null ? itemForUpdate.isAvailable()
                : itemDto.getAvailable());

        return allItemsById.put(itemForUpdate.getId(), itemForUpdate);
    }

    @Override
    public Item deleteItemById(long itemId) {
        getItemOrThrow(itemId, Actions.TO_DELETE);
        return allItemsById.remove(itemId);
    }

    @Override
    public Collection<Item> deleteAllItemsFromOwner(long userId) {
        Collection<Item> allItemsByOwnerId = getAllItemsByOwnerId(userId);

        allItemsByOwnerId.stream()
                .map(Item::getId)
                .peek(allItemsById::remove)
                .toList();

        return allItemsByOwnerId;
    }

    private long getNewId() {
        return ++id;
    }

    private Item getItemOrThrow(long itemId, String message) {
        Optional<Item> optionalItem = Optional.ofNullable(allItemsById.get(itemId));
        if (optionalItem.isEmpty()) {
            throw new NotFoundException(String.format("Вещи с id = %d для %s не найдено", itemId, message));
        }
        return optionalItem.get();
    }

    private boolean hasTextInDesc(Item item, String text) {
        return item.getName().toLowerCase().contains(text.toLowerCase())
                || item.getDescription().toLowerCase().contains(text.toLowerCase());
    }
}
