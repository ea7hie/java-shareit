package ru.practicum.shareit.item.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@UtilityClass
public class ItemMapper {
    public ItemDto toItemDto(Item item, List<CommentDto> comments) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getOwnerId(),
                item.getRequestId(),
                item.isAvailable(),
                comments
        );
    }

    public ItemDtoForOwner toItemDtoForOwner(Item item, BookingDto last, BookingDto next, List<CommentDto> comments) {
        return new ItemDtoForOwner(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                last,
                next,
                comments
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
