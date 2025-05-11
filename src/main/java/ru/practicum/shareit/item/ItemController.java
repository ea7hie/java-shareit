package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {
    private ItemService itemService;

    @PostMapping
    public ItemDto createNewItem(@RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") long ownerId) {
        return itemService.createItem(itemDto, ownerId);
    }

    @GetMapping
    public Collection<ItemDto> getAllItems() {
        return itemService.getAllItemsDto();
    }

    @GetMapping
    public Collection<ItemDto> getItemByOwnerId(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getAllItemsByOwnerId(userId);
    }

    @GetMapping("/{idOfItem}")
    public ItemDto getItemById(@PathVariable long idOfItem) {
        return itemService.getItemDtoById(idOfItem);
    }

    @GetMapping("/search?text={text}")
    public Collection<ItemDto> getItemBySearch(@RequestParam String text) {
        return itemService.getAllItemBySearch(text);
    }

    @GetMapping("/owner/{ownerId}/search?text={text}")
    public Collection<ItemDto> getItemBySearch(@PathVariable long ownerId, @RequestParam String text) {
        return itemService.getAllItemsByOwnerIdAndSearch(ownerId, text);
    }

    @PatchMapping("/{idOfItem}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") long ownerId) {
        return itemService.updateItem(itemDto, ownerId);
    }

    @DeleteMapping("/{idOfItem}")
    public ItemDto deleteItem(@PathVariable long idOfItem, @RequestHeader("X-Sharer-User-Id") long ownerId) {
        return itemService.deleteItemById(idOfItem, ownerId);
    }

    @DeleteMapping
    public Collection<ItemDto> deleteAllItemsByOwnerId(@RequestHeader("X-Sharer-User-Id") long ownerId) {
        return itemService.deleteAllItemsFromOwner(ownerId);
    }
}
