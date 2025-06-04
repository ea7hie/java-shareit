package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dao.RequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.user.dao.UserChecks;
import ru.practicum.shareit.user.dao.UserRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    @Override
    public ItemRequestDto createItemRequest(ItemRequestDto itemRequestDto, long userId) {
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);
        return ItemRequestMapper.toItemRequestDto(requestRepository.createItemRequest(itemRequest));
    }

    @Override
    public Collection<ItemRequestDto> getAllItemRequest() {
        return requestRepository.getAllItemRequest().stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .toList();
    }

    @Override
    public ItemRequestDto getItemRequestById(long itemRequestId) {
        return ItemRequestMapper.toItemRequestDto(requestRepository.getItemRequestById(itemRequestId));

    }

    @Override
    public Collection<ItemRequestDto> getAllItemRequestsFromRequester(long requesterId) {
        UserChecks.isUserExistsById(userRepository, requesterId);
        return requestRepository.getAllItemRequestsFromRequester(requesterId).stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .toList();
    }

    @Override
    public ItemRequestDto updateItemRequest(ItemRequestDto itemRequestDtoForUpdate, long userId, long itemReqId) {
        UserChecks.isUserExistsById(userRepository, userId);
        itemRequestDtoForUpdate.setId(itemReqId);
        return ItemRequestMapper.toItemRequestDto(requestRepository.updateItemRequest(itemRequestDtoForUpdate, userId));
    }

    @Override
    public ItemRequestDto deleteItemRequest(long itemRequestIdForDelete, long userId) {
        UserChecks.isUserExistsById(userRepository, userId);
        return ItemRequestMapper.toItemRequestDto(requestRepository.deleteItemRequest(itemRequestIdForDelete, userId));

    }
}
