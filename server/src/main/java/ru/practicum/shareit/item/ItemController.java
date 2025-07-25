package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentDtoForCreate;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForOwner;

import java.util.Collection;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final String headerOfUserId = "X-Sharer-User-Id";

    @PostMapping
    public ItemDto createNewItem(@RequestBody ItemDto itemDto, @RequestHeader(headerOfUserId) long ownerId) {
        return itemService.createItem(itemDto, ownerId);
    }

    @GetMapping
    public Collection<ItemDtoForOwner> getItemByOwnerId(@RequestHeader(headerOfUserId) long userId) {
        return itemService.getAllItemsByOwnerId(userId);
    }

    @GetMapping("/{idOfItem}")
    public ItemDtoForOwner getItemById(@PathVariable long idOfItem, @RequestHeader(headerOfUserId) long ownerId) {
        return itemService.getItemDtoById(idOfItem, ownerId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> getItemBySearch(@RequestParam String text) {
        return itemService.getAllItemBySearch(text.trim());
    }

    @PatchMapping("/{idOfItem}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto, @RequestHeader(headerOfUserId) long ownerId,
                              @PathVariable long idOfItem) {
        return itemService.updateItem(itemDto, ownerId, idOfItem);
    }

    @DeleteMapping("/{idOfItem}")
    public ItemDto deleteItem(@PathVariable long idOfItem, @RequestHeader(headerOfUserId) long ownerId) {
        return itemService.deleteItemById(idOfItem, ownerId);
    }

    @DeleteMapping
    public Collection<ItemDto> deleteAllItemsByOwnerId(@RequestHeader(headerOfUserId) long ownerId) {
        return itemService.deleteAllItemsFromOwner(ownerId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createNewComment(@RequestBody CommentDtoForCreate commentDtoForCreate,
                                       @RequestHeader(headerOfUserId) long authorId, @PathVariable long itemId) {
        return itemService.createNewComment(commentDtoForCreate, authorId, itemId);
    }
}
