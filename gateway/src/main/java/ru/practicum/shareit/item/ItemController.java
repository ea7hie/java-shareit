package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.comment.CommentDtoForCreate;
import ru.practicum.shareit.item.dto.ItemDto;

@Controller
@Validated
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient itemClient;
    private final String headerOfUserId = BaseClient.headerOfUserId;

    @PostMapping
    public ResponseEntity<Object> createNewItem(@RequestHeader(headerOfUserId) long ownerId,
                                                @RequestBody ItemDto itemDto) {
        return itemClient.createItem(itemDto, ownerId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemByOwnerId(@RequestHeader(headerOfUserId) long userId) {
        return itemClient.getAllItemsByOwnerId(userId);
    }

    @GetMapping("/{idOfItem}")
    public ResponseEntity<Object> getItemById(@Positive @PathVariable long idOfItem,
                                              @RequestHeader(headerOfUserId) long ownerId) {
        return itemClient.getItemDtoById(idOfItem, ownerId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getItemBySearch(@RequestParam("text") String text,
                                                  @RequestHeader(headerOfUserId) long userId) {
        return itemClient.getAllItemBySearch(text.trim(), userId);
    }

    @PatchMapping("/{idOfItem}")
    public ResponseEntity<Object> updateItem(@RequestBody ItemDto itemDto,
                                             @RequestHeader(headerOfUserId) long ownerId,
                                             @Positive @PathVariable long idOfItem) {
        return itemClient.updateItem(itemDto, ownerId, idOfItem);
    }

    @DeleteMapping("/{idOfItem}")
    public ResponseEntity<Object> deleteItem(@Positive @PathVariable long idOfItem,
                                             @RequestHeader(headerOfUserId) long ownerId) {
        return itemClient.deleteItemById(idOfItem, ownerId);
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteAllItemsByOwnerId(@RequestHeader(headerOfUserId) long ownerId) {
        return itemClient.deleteAllItemsFromOwner(ownerId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createNewComment(@RequestBody @Valid CommentDtoForCreate commentDtoForCreate,
                                                   @RequestHeader(headerOfUserId) long authorId,
                                                   @Positive @PathVariable long itemId) {
        return itemClient.createNewComment(commentDtoForCreate, authorId, itemId);
    }
}
