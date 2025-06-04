package ru.practicum.shareit.item.dao;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.enums.Actions;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.Optional;

@UtilityClass
public class ItemChecks {
    public void isItemExistsById(ItemRepository itemRepository, long itemIdForCheck) {
        Optional<Item> optionalItem = itemRepository.findById(itemIdForCheck);
        if (optionalItem.isEmpty()) {
            throw new NotFoundException(String.format("Вещи с id = %d для %s не найдено", itemIdForCheck,
                    Actions.TO_VIEW));
        }
    }

    public Item getItemOrThrow(ItemRepository itemRepository, long itemId, String message) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isEmpty()) {
            throw new NotFoundException(String.format("Вещи с id = %d для %s не найдено", itemId, message));
        }
        return optionalItem.get();
    }
}
