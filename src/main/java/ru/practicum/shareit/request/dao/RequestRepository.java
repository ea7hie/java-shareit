package ru.practicum.shareit.request.dao;

import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Collection;

public interface RequestRepository {
    ItemRequest createItemRequest(ItemRequest itemRequest);

    Collection<ItemRequest> getAllItemRequest();

    ItemRequest getItemRequestById(long itemRequestId);

    Collection<ItemRequest> getAllItemRequestsFromRequester(long requesterId);

    ItemRequest updateItemRequest(ItemRequestDto itemRequestDtoForUpdate, long userId);

    ItemRequest deleteItemRequest(long itemRequestIdForDelete, long userId);
}
