package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoForCreate;

import java.util.Collection;

public interface RequestService {
    ItemRequestDto createItemRequest(ItemRequestDtoForCreate itemRequestDto, long userId);

    Collection<ItemRequestDto> getAllItemRequest();

    ItemRequestDto getItemRequestById(long itemRequestId);

    Collection<ItemRequestDto> getAllItemRequestsFromRequester(long requesterId);

    ItemRequestDto updateItemRequest(ItemRequestDtoForCreate itemRequestDtoForUpdate, long userId, long itemReqId);

    ItemRequestDto deleteItemRequest(long itemRequestIdForDelete, long userId);
}