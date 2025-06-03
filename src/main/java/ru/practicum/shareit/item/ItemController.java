package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final String headerOfUserId = "X-Sharer-User-Id";

    @PostMapping
    public ItemDto createNewItem(@Valid @RequestBody ItemDto itemDto, @RequestHeader(headerOfUserId) long ownerId) {
        return itemService.createItem(itemDto, ownerId);
    }

    @GetMapping
    public Collection<ItemDto> getItemByOwnerId(@RequestHeader(headerOfUserId) long userId) {
        return itemService.getAllItemsByOwnerId(userId);
    }

    @GetMapping("/{idOfItem}")
    public ItemDto getItemById(@PathVariable long idOfItem) {
        return itemService.getItemDtoById(idOfItem);
    }

    @GetMapping("/search")
    public Collection<ItemDto> getItemBySearch(@RequestParam String text) {
        return itemService.getAllItemBySearch(text.trim());
    }

    @GetMapping("/owner/{ownerId}/search")
    public Collection<ItemDto> getItemBySearch(@PathVariable long ownerId, @RequestParam String text) {
        return itemService.getAllItemsByOwnerIdAndSearch(ownerId, text.trim());
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
}
