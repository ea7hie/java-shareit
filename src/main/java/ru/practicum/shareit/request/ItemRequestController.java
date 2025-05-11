package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Collection;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {
    private RequestService requestService;

    @PostMapping
    public ItemRequestDto createItemRequest(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                            @RequestHeader("X-Sharer-User-Id") long userId) {
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

    @PatchMapping
    public ItemRequestDto updateItemRequest(ItemRequestDto itemRequestDtoForUpdate, long userId) {
        return requestService.updateItemRequest(itemRequestDtoForUpdate, userId);
    }

    @DeleteMapping
    public ItemRequestDto deleteItemRequest(long itemRequestIdForDelete, long userId) {
        return requestService.deleteItemRequest(itemRequestIdForDelete, userId);
    }
}
