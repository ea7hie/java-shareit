package ru.practicum.shareit.request.dao;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.enums.Actions;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.request.ItemRequest;

import java.util.Optional;

@UtilityClass
public class RequestChecks {
    public void isItemRequestExistsById(RequestRepository requestRepository, long requestIdForCheck) {
        Optional<ItemRequest> optionalItemRequest = requestRepository.findById(requestIdForCheck);
        if (optionalItemRequest.isEmpty()) {
            throw new NotFoundException(String.format("Запроса с id = %d для %s не найдено", requestIdForCheck,
                    Actions.TO_VIEW));
        }
    }

    public ItemRequest getItemRequestOrThrow(RequestRepository requestRepository, long requestId, String message) {
        Optional<ItemRequest> optionalItemRequest = requestRepository.findById(requestId);
        if (optionalItemRequest.isEmpty()) {
            throw new NotFoundException(String.format("Запроса с id = %d для %s не найдено", requestId, message));
        }
        return optionalItemRequest.get();
    }
}
