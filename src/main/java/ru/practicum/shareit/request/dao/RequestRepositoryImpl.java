package ru.practicum.shareit.request.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.model.AccessError;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class RequestRepositoryImpl implements RequestRepository {
    private Map<Long, ItemRequest> allItemRequestById = new HashMap<>();
    private long id = 0;

    @Override
    public ItemRequest createItemRequest(ItemRequest itemRequest) {
        itemRequest.setId(getNewId());
        return allItemRequestById.put(itemRequest.getId(), itemRequest);
    }

    @Override
    public Collection<ItemRequest> getAllItemRequest() {
        return allItemRequestById.values();
    }

    @Override
    public ItemRequest getItemRequestById(long itemRequestId) {
        return getItemRequestOrThrow(itemRequestId, "отображения");
    }

    @Override
    public Collection<ItemRequest> getAllItemRequestsFromRequester(long requesterId) {
        return allItemRequestById.values().stream()
                .filter(itemRequest -> itemRequest.getRequester().getId() == requesterId)
                .toList();
    }

    @Override
    public ItemRequest updateItemRequest(ItemRequestDto itemRequestDtoForUpdate, long userId) {
        ItemRequest itemReqForUpdate = getItemRequestOrThrow(itemRequestDtoForUpdate.getId(), "обновления");
        if (itemReqForUpdate.getRequester().getId() == userId) {
            itemReqForUpdate.setDescription(itemRequestDtoForUpdate.getDescription() == null ?
                    itemReqForUpdate.getDescription() : itemRequestDtoForUpdate.getDescription());
        } else {
            throw new AccessError("У вас нет пра доступа к редактированию этого запроса");
        }

        return allItemRequestById.put(itemReqForUpdate.getId(), itemReqForUpdate);
    }

    @Override
    public ItemRequest deleteItemRequest(long itemRequestIdForDelete, long userId) {
        ItemRequest itemRequestForDelete = getItemRequestOrThrow(itemRequestIdForDelete, "удаления");
        if (itemRequestForDelete.getRequester().getId() != userId) {
            throw new AccessError("У вас нет пра доступа к удалению этого запроса");
        }
        return allItemRequestById.remove(itemRequestIdForDelete);
    }

    private long getNewId() {
        return ++id;
    }

    private ItemRequest getItemRequestOrThrow(long itemRequestId, String message) {
        Optional<ItemRequest> optionalItemRequest = Optional.ofNullable(allItemRequestById.get(itemRequestId));
        if (optionalItemRequest.isEmpty()) {
            throw new NotFoundException(String.format("Запроса с id = %d для %s не найдено", itemRequestId, message));
        }
        return optionalItemRequest.get();
    }
}
