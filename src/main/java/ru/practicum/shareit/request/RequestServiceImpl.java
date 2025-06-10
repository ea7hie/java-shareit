package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.enums.Actions;
import ru.practicum.shareit.exception.model.AccessError;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.request.dao.RequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserRepository;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    private final String messageCantUpdate = "У вас нет прав доступа к редактированию этого запроса.";
    private final String messageCantDelete = "У вас нет прав доступа к удалению этого запроса.";

    @Override
    public ItemRequestDto createItemRequest(ItemRequestDto itemRequestDto, long userId) {
        isUserExistsById(userId);
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
        return ItemRequestMapper.toItemRequestDto(getItemRequestOrThrow(itemRequestId, Actions.TO_VIEW));

    }

    @Override
    public Collection<ItemRequestDto> getAllItemRequestsFromRequester(long requesterId) {
        isUserExistsById(requesterId);
        return requestRepository.findAllByRequesterId(requesterId).stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .toList();
    }

    @Override
    public ItemRequestDto updateItemRequest(ItemRequestDto itemRequestDtoForUpdate, long userId, long itemReqId) {
        ItemRequest itemReqForUpdate = getItemRequestOrThrow(itemReqId, Actions.TO_UPDATE);

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
        ItemRequest itemRequestForDelete = getItemRequestOrThrow(itemRequestIdForDelete, Actions.TO_DELETE);

        if (itemRequestForDelete.getRequesterId() == userId) {
            requestRepository.deleteById(itemRequestIdForDelete);
            return ItemRequestMapper.toItemRequestDto(itemRequestForDelete);
        }
        throw new AccessError(messageCantDelete);
    }

    private ItemRequest getItemRequestOrThrow(long requestId, String message) {
        Optional<ItemRequest> optionalItemRequest = requestRepository.findById(requestId);
        if (optionalItemRequest.isEmpty()) {
            throw new NotFoundException(String.format("Запроса с id = %d для %s не найдено", requestId, message));
        }
        return optionalItemRequest.get();
    }

    private void isUserExistsById(long userIdForCheck) {
        Optional<User> optionalUser = userRepository.findById(userIdForCheck);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException(String.format("Пользователя с id = %d для %s не найдено", userIdForCheck,
                    Actions.TO_VIEW));
        }
    }
}
