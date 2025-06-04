package ru.practicum.shareit.item.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.model.Item;

@UtilityClass
public class ItemMapper {
    public ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getOwnerId(),
                item.getRequestId(),
                item.isAvailable()
        );
    }

    public ItemDtoForOwner toItemDtoForOwner(Item item, BookingDto last, BookingDto next) {
        return new ItemDtoForOwner(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                last,
                next
        );
    }

    public Item toItem(ItemDto itemDto) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getOwnerId(),
                itemDto.getRequestId(),
                itemDto.getAvailable()
        );
    }
}
