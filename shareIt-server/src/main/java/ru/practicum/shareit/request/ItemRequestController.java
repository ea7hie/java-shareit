package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoForCreate;

import java.util.Collection;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final RequestService requestService;
    private final String headerOfUserId = "X-Sharer-User-Id";

    @PostMapping
    public ItemRequestDto createItemRequest(@Valid @RequestBody ItemRequestDtoForCreate itemRequestDto,
                                            @RequestHeader(headerOfUserId) long userId) {
        return requestService.createItemRequest(itemRequestDto, userId);
    }

    @GetMapping
    public Collection<ItemRequestDto> getAllItemRequest(@RequestHeader(headerOfUserId) long requesterId) {
        return requestService.getAllItemRequestsFromRequester(requesterId);
    }

    @GetMapping("/all")
    public Collection<ItemRequestDto> getAllItemRequest() {
        return requestService.getAllItemRequest();
    }

    @GetMapping("/{itemRequestId}")
    public ItemRequestDto getItemRequestById(@PathVariable long itemRequestId) {
        return requestService.getItemRequestById(itemRequestId);
    }

    @PatchMapping("/{itemReqId}")
    public ItemRequestDto updateItemRequest(ItemRequestDtoForCreate itemRequestDtoForUpdate,
                                            long userId, @PathVariable long itemReqId) {
        return requestService.updateItemRequest(itemRequestDtoForUpdate, userId, itemReqId);
    }

    @DeleteMapping
    public ItemRequestDto deleteItemRequest(long itemRequestIdForDelete, long userId) {
        return requestService.deleteItemRequest(itemRequestIdForDelete, userId);
    }
}
