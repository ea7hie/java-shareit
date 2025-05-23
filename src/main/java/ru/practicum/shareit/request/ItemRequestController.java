package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Collection;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final RequestService requestService;
    private final String headerOfUserId = "X-Sharer-User-Id";

    @PostMapping
    public ItemRequestDto createItemRequest(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                            @RequestHeader(headerOfUserId) long userId) {
        return requestService.createItemRequest(itemRequestDto, userId);
    }

    @GetMapping
    public Collection<ItemRequestDto> getAllItemRequest() {
        return requestService.getAllItemRequest();
    }

    @GetMapping("/{itemRequestId}")
    public ItemRequestDto getItemRequestById(long itemRequestId) {
        return requestService.getItemRequestById(itemRequestId);
    }

    @GetMapping("/user/{requesterId}")
    public Collection<ItemRequestDto> getAllItemRequestsFromRequester(long requesterId) {
        return requestService.getAllItemRequestsFromRequester(requesterId);
    }

    @PatchMapping("/{itemReqId}")
    public ItemRequestDto updateItemRequest(ItemRequestDto itemRequestDtoForUpdate,
                                            long userId, @PathVariable long itemReqId) {
        return requestService.updateItemRequest(itemRequestDtoForUpdate, userId, itemReqId);
    }

    @DeleteMapping
    public ItemRequestDto deleteItemRequest(long itemRequestIdForDelete, long userId) {
        return requestService.deleteItemRequest(itemRequestIdForDelete, userId);
    }
}
