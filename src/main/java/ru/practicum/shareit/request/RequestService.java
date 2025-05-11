package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Collection;

public interface RequestService {
    ItemRequestDto createItemRequest(ItemRequestDto itemRequestDto, long userId);

    Collection<ItemRequestDto> getAllItemRequest();

    ItemRequestDto getItemRequestById(long itemRequestId);

    Collection<ItemRequestDto> getAllItemRequestsFromRequester(long requesterId);

    ItemRequestDto updateItemRequest(ItemRequestDto itemRequestDtoForUpdate, long userId);

    ItemRequestDto deleteItemRequest(long itemRequestIdForDelete, long userId);
}