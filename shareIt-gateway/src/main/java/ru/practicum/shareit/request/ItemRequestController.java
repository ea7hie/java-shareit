package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.ItemRequestDtoForCreate;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final RequestClient requestClient;
    private final String headerOfUserId = BaseClient.headerOfUserId;

    @PostMapping
    public ResponseEntity<Object> createItemRequest(@RequestBody @Valid ItemRequestDtoForCreate itemRequestDto,
                                                    @RequestHeader(headerOfUserId) long userId) {
        return requestClient.createItemRequest(itemRequestDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemRequestsFromRequester(@RequestHeader(headerOfUserId) long requesterId) {
        return requestClient.getAllItemRequestsFromRequester(requesterId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequest(@RequestHeader(headerOfUserId) long userId) {
        return requestClient.getAllItemRequest(userId);
    }

    @GetMapping("/{itemRequestId}")
    public ResponseEntity<Object> getItemRequestById(@Positive @PathVariable long itemRequestId,
                                                     @RequestHeader(headerOfUserId) long requesterId) {
        return requestClient.getItemRequestById(itemRequestId, requesterId);
    }

    @PatchMapping("/{itemReqId}")
    public ResponseEntity<Object> updateItemRequest(@RequestBody @Valid ItemRequestDtoForCreate itemRequestDtoForUpdate,
                                                    @RequestHeader(headerOfUserId) long userId,
                                                    @Positive @PathVariable long itemReqId) {
        return requestClient.updateItemRequest(itemRequestDtoForUpdate, userId, itemReqId);
    }

    @DeleteMapping("/{itemRequestIdForDelete}")
    public ResponseEntity<Object> deleteItemRequest(@Positive @PathVariable long itemRequestIdForDelete,
                                                    @RequestHeader(headerOfUserId) long userId) {
        return requestClient.deleteItemRequest(itemRequestIdForDelete, userId);
    }
}
