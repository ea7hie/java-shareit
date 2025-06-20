package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;
import ru.practicum.shareit.request.ItemRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ItemRequestMapperTest {

    @Test
    void toItemRequestDto_ShouldMapAllFields() {
        LocalDateTime created = LocalDateTime.of(2025, 6, 17, 15, 0);
        ItemRequest itemRequest = new ItemRequest(10L, "Test description", 5L, created);

        ItemDtoForRequest itemDto1 = new ItemDtoForRequest(); // Заполни поля, если есть
        ItemDtoForRequest itemDto2 = new ItemDtoForRequest();

        List<ItemDtoForRequest> items = List.of(itemDto1, itemDto2);

        ItemRequestDto dto = ItemRequestMapper.toItemRequestDto(itemRequest, items);

        assertThat(dto.getId()).isEqualTo(10L);
        assertThat(dto.getDescription()).isEqualTo("Test description");
        assertThat(dto.getRequesterId()).isEqualTo(5L);
        assertThat(dto.getCreated()).isEqualTo(created);
        assertThat(dto.getItems()).hasSize(2).containsExactly(itemDto1, itemDto2);
    }

    @Test
    void toItemRequest_ShouldMapAllFields() {
        ItemRequestDtoForCreate dtoForCreate = new ItemRequestDtoForCreate("New request");
        long requesterId = 7L;

        ItemRequest entity = ItemRequestMapper.toItemRequest(dtoForCreate, requesterId);

        assertThat(entity.getId()).isEqualTo(-1L);
        assertThat(entity.getDescription()).isEqualTo("New request");
        assertThat(entity.getRequesterId()).isEqualTo(7L);
        assertThat(entity.getCreated()).isNotNull();
        assertThat(entity.getCreated()).isBeforeOrEqualTo(LocalDateTime.now());
    }
}