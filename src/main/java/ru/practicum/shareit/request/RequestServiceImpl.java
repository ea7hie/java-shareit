package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.enums.Actions;
import ru.practicum.shareit.exception.model.AccessError;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.RequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoForCreate;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;

    private final String messageCantUpdate = "У вас нет прав доступа к редактированию этого запроса.";
    private final String messageCantDelete = "У вас нет прав доступа к удалению этого запроса.";

    @Override
    public ItemRequestDto createItemRequest(ItemRequestDtoForCreate itemRequestDto, long userId) {
        isUserExistsById(userId);
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto, userId);
        return ItemRequestMapper.toItemRequestDto(requestRepository.save(itemRequest), null);
    }

    @Override
    public Collection<ItemRequestDto> getAllItemRequest() {
        return requestRepository.findAll().stream()
                .map(request -> ItemRequestMapper.toItemRequestDto(request, getItemDto(request.getId())))
                .toList();
    }

    @Override
    public ItemRequestDto getItemRequestById(long itemRequestId) {
        ItemRequest itemRequest = getItemRequestOrThrow(itemRequestId, Actions.TO_VIEW);
        return ItemRequestMapper.toItemRequestDto(itemRequest, getItemDto(itemRequestId));

    }

    @Override
    public Collection<ItemRequestDto> getAllItemRequestsFromRequester(long requesterId) {
        isUserExistsById(requesterId);
        return requestRepository.findAllByRequesterId(requesterId).stream()
                .map(request -> ItemRequestMapper.toItemRequestDto(request, getItemDto(request.getId())))
                .toList();
    }

    @Override
    public ItemRequestDto updateItemRequest(ItemRequestDtoForCreate itemReqDtoForUpdate, long userId, long itemReqId) {
        ItemRequest itemReqForUpdate = getItemRequestOrThrow(itemReqId, Actions.TO_UPDATE);

        if (itemReqForUpdate.getRequesterId() == userId) {
            itemReqForUpdate.setDescription(itemReqDtoForUpdate.getDescription() == null ?
                    itemReqForUpdate.getDescription() : itemReqDtoForUpdate.getDescription());

            requestRepository.updateItemRequest(itemReqId, itemReqForUpdate.getDescription());
            return ItemRequestMapper.toItemRequestDto(itemReqForUpdate, getItemDto(itemReqId));
        }
        throw new AccessError(messageCantUpdate);
    }

    @Override
    public ItemRequestDto deleteItemRequest(long itemRequestIdForDelete, long userId) {
        ItemRequest itemRequestForDelete = getItemRequestOrThrow(itemRequestIdForDelete, Actions.TO_DELETE);

        if (itemRequestForDelete.getRequesterId() == userId) {
            requestRepository.deleteById(itemRequestIdForDelete);
            return ItemRequestMapper.toItemRequestDto(itemRequestForDelete, getItemDto(itemRequestIdForDelete));
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

    private List<ItemDto> getItemDto(long requestId) {
        List<Item> items = itemRepository.findAllByRequestId(requestId);

        List<Long> itemsIds = items.stream().map(Item::getId).toList();
        Collection<Comment> allComments = getCommentsByItemIds(itemsIds);

        return items.stream()
                .map(item -> ItemMapper.toItemDto(item,
                        getCommentDtosForOneItemDtoFromComments(allComments, item.getId())))
                .toList();
    }

    private Collection<Comment> getCommentsByItemIds(Collection<Long> itemIds) {
        return commentRepository.findAllByItemIdIn(itemIds);
    }

    private List<CommentDto> getCommentDtosForOneItemDtoFromComments(Collection<Comment> allComments, long itemID) {
        List<Comment> commentsFromItem = allComments.stream()
                .filter(comment -> comment.getItem().getId() == itemID)
                .toList();

        allComments.removeAll(commentsFromItem);

        return commentsFromItem.stream()
                .map(CommentMapper::toCommentDto)
                .toList();
    }
}
