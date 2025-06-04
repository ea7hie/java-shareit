package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.enums.Actions;
import ru.practicum.shareit.exception.model.AccessError;
import ru.practicum.shareit.request.dao.RequestChecks;
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

    private final String messageCantUpdate = "У вас нет прав доступа к редактированию этого запроса.";
    private final String messageCantDelete = "У вас нет прав доступа к удалению этого запроса.";

    @Override
    public ItemRequestDto createItemRequest(ItemRequestDto itemRequestDto, long userId) {
        UserChecks.isUserExistsById(userRepository, userId);
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);
        return ItemRequestMapper.toItemRequestDto(requestRepository.save(itemRequest));
    }

    @Override
    public Collection<ItemRequestDto> getAllItemRequest() {
        return requestRepository.findAll().stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .toList();
    }

    @Override
    public ItemRequestDto getItemRequestById(long itemRequestId) {
        return ItemRequestMapper.toItemRequestDto(RequestChecks.getItemRequestOrThrow(requestRepository,
                itemRequestId, Actions.TO_VIEW));

    }

    @Override
    public Collection<ItemRequestDto> getAllItemRequestsFromRequester(long requesterId) {
        UserChecks.isUserExistsById(userRepository, requesterId);
        return requestRepository.findAllByRequesterId(requesterId).stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .toList();
    }

    @Override
    public ItemRequestDto updateItemRequest(ItemRequestDto itemRequestDtoForUpdate, long userId, long itemReqId) {
        ItemRequest itemReqForUpdate = RequestChecks.getItemRequestOrThrow(requestRepository,
                itemReqId, Actions.TO_UPDATE);

        if (itemReqForUpdate.getRequesterId() == userId) {
            itemReqForUpdate.setDescription(itemRequestDtoForUpdate.getDescription() == null ?
                    itemReqForUpdate.getDescription() : itemRequestDtoForUpdate.getDescription());

            requestRepository.updateItemRequest(itemReqId, itemReqForUpdate.getDescription());
            return ItemRequestMapper.toItemRequestDto(itemReqForUpdate);
        }
        throw new AccessError(messageCantUpdate);
    }

    @Override
    public ItemRequestDto deleteItemRequest(long itemRequestIdForDelete, long userId) {
        ItemRequest itemRequestForDelete = RequestChecks.getItemRequestOrThrow(requestRepository,
                itemRequestIdForDelete, Actions.TO_DELETE);

        if (itemRequestForDelete.getRequesterId() == userId) {
            requestRepository.deleteById(itemRequestIdForDelete);
            return ItemRequestMapper.toItemRequestDto(itemRequestForDelete);
        }
        throw new AccessError(messageCantDelete);
    }
}
