package ru.practicum.shareit.item;

import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentDtoForCreate;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForOwner;

import java.util.Collection;

public interface ItemService {
    ItemDto createItem(ItemDto itemDto, long userId);

    ItemDtoForOwner getItemDtoById(long itemDtoId, long ownerId);

    Collection<ItemDtoForOwner> getAllItemsByOwnerId(long userId);

    Collection<ItemDto> getAllItemBySearch(String text);

    ItemDto updateItem(ItemDto itemDto, long ownerId, long idOfItem);

    ItemDto deleteItemById(long itemId, long ownerId);

    Collection<ItemDto> deleteAllItemsFromOwner(long userId);

    CommentDto createNewComment(CommentDtoForCreate commentDtoForCreate, long authorId, long itemId);
}
