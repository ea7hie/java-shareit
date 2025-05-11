package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private Map<Long, Item> allItemsById = new HashMap<>();

    @Override
    public Item get(long itemId) {
        Optional<Item> optionalItem= Optional.ofNullable(allItemsById.get(itemId));
        if (optionalItem.isEmpty()) {
            throw new NotFoundException(String.format("Вещи с id = %d для отображения не найдено", itemId));
        }
        return optionalItem.get();
    }
}
