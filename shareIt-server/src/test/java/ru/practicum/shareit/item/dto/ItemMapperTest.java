package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ItemMapperTest {

    @Test
    void toItemDto_ShouldMapCorrectly() {
        Item item = new Item(1L, "itemName", "desc", 10L, 20L, true);
        List<CommentDto> comments = List.of(new CommentDto(1L, "comment text", "author", null));

        ItemDto itemDto = ItemMapper.toItemDto(item, comments);

        assertThat(itemDto.getId()).isEqualTo(item.getId());
        assertThat(itemDto.getName()).isEqualTo(item.getName());
        assertThat(itemDto.getDescription()).isEqualTo(item.getDescription());
        assertThat(itemDto.getOwnerId()).isEqualTo(item.getOwnerId());
        assertThat(itemDto.getRequestId()).isEqualTo(item.getRequestId());
        assertThat(itemDto.getAvailable()).isEqualTo(item.isAvailable());
        assertThat(itemDto.getComments()).isEqualTo(comments);
    }

    @Test
    void toItemDtoForOwner_ShouldMapCorrectly() {
        Item item = new Item(1L, "itemName", "desc", 10L, 20L, true);
        BookingDto last = new BookingDto(1L, null, null, null, null, null);
        BookingDto next = new BookingDto(2L, null, null, null, null, null);
        List<CommentDto> comments = List.of();

        ItemDtoForOwner dtoForOwner = ItemMapper.toItemDtoForOwner(item, last, next, comments);

        assertThat(dtoForOwner.getId()).isEqualTo(item.getId());
        assertThat(dtoForOwner.getName()).isEqualTo(item.getName());
        assertThat(dtoForOwner.getDescription()).isEqualTo(item.getDescription());
        assertThat(dtoForOwner.getAvailable()).isEqualTo(item.isAvailable());
        assertThat(dtoForOwner.getLastBooking()).isEqualTo(last);
        assertThat(dtoForOwner.getNextBooking()).isEqualTo(next);
        assertThat(dtoForOwner.getComments()).isEqualTo(comments);
    }

    @Test
    void toItem_ShouldMapCorrectly() {
        ItemDto itemDto = new ItemDto(1L, "itemName", "desc", 10L, 20L, true, List.of());

        Item item = ItemMapper.toItem(itemDto);

        assertThat(item.getId()).isEqualTo(itemDto.getId());
        assertThat(item.getName()).isEqualTo(itemDto.getName());
        assertThat(item.getDescription()).isEqualTo(itemDto.getDescription());
        assertThat(item.getOwnerId()).isEqualTo(itemDto.getOwnerId());
        assertThat(item.getRequestId()).isEqualTo(itemDto.getRequestId());
        assertThat(item.isAvailable()).isEqualTo(itemDto.getAvailable());
    }

    @Test
    void toItemDtoForRequest_ShouldMapCorrectly() {
        Item item = new Item(1L, "itemName", "desc", 10L, 20L, true);

        ItemDtoForRequest dtoForRequest = ItemMapper.toItemDtoForRequest(item);

        assertThat(dtoForRequest.getId()).isEqualTo(item.getId());
        assertThat(dtoForRequest.getName()).isEqualTo(item.getName());
        assertThat(dtoForRequest.getOwnerId()).isEqualTo(item.getOwnerId());
    }
}