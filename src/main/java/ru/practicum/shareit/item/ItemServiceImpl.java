package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.enums.Actions;
import ru.practicum.shareit.exception.model.AccessError;
import ru.practicum.shareit.item.dao.ItemChecks;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserChecks;
import ru.practicum.shareit.user.dao.UserRepository;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    private final String messageCantDelete = "Удалять данные о вещи может только владелец.";
    private final String messageCantUpdate = "Редактировать данные о вещи может только владелец.";

    @Override
    @Transactional
    public ItemDto createItem(ItemDto itemDto, long userId) {
        UserChecks.isUserExistsById(userRepository, userId);
        itemDto.setOwnerId(userId);
        return ItemMapper.toItemDto(itemRepository.save(ItemMapper.toItem(itemDto)));
    }

    @Override
    public Collection<ItemDto> getAllItemsDto() {
        return itemRepository.findAll().stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public ItemDto getItemDtoById(long itemDtoId) {
        return ItemMapper.toItemDto(ItemChecks.getItemOrThrow(itemRepository, itemDtoId, Actions.TO_VIEW));
    }

    @Override
    public Collection<ItemDto> getAllItemsByOwnerId(long userId) {
        UserChecks.isUserExistsById(userRepository, userId);

        return itemRepository.findAllByOwnerId(userId).stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public Collection<ItemDto> getAllItemBySearch(String text) {
        if (text.isEmpty()) {
            return List.of();
        }

        return itemRepository
                .findByDescriptionContainsIgnoreCaseAndIsAvailableIsTrueOrNameContainsIgnoreCaseAndIsAvailableIsTrue(
                        text, text)
                .stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    @Transactional
    public ItemDto updateItem(ItemDto itemDto, long ownerId, long idOfItem) {
        UserChecks.isUserExistsById(userRepository, ownerId);

        Item itemForUpdate = ItemChecks.getItemOrThrow(itemRepository, idOfItem, Actions.TO_UPDATE);

        if (itemForUpdate.getOwnerId() != ownerId) {
            throw new AccessError(messageCantUpdate);
        }

       // itemDto.setId(idOfItem);

        itemForUpdate.setName(itemDto.getName() == null ? itemForUpdate.getName() : itemDto.getName());
        itemForUpdate.setDescription(itemDto.getDescription() == null ? itemForUpdate.getDescription()
                : itemDto.getDescription());
        itemForUpdate.setAvailable(itemDto.getAvailable() == null ? itemForUpdate.isAvailable()
                : itemDto.getAvailable());

        itemRepository.updateItem(idOfItem, itemForUpdate.getName(), itemForUpdate.getDescription(),
                itemForUpdate.isAvailable());

        return ItemMapper.toItemDto(itemForUpdate);
    }

    @Override
    @Transactional
    public ItemDto deleteItemById(long itemId, long ownerId) {
        UserChecks.isUserExistsById(userRepository, ownerId);

        Item itemForDelete = ItemChecks.getItemOrThrow(itemRepository, itemId, Actions.TO_DELETE);
        if (itemForDelete.getOwnerId() == ownerId) {
            itemRepository.deleteById(itemId);
            return ItemMapper.toItemDto(itemForDelete);
        }
        throw new AccessError(messageCantDelete);
    }

    @Override
    @Transactional
    public Collection<ItemDto> deleteAllItemsFromOwner(long userId) {
        UserChecks.isUserExistsById(userRepository, userId);

        Collection<Item> allByOwnerId = itemRepository.findAllByOwnerId(userId);

        itemRepository.deleteAllItemsFromOwner(userId);

        return allByOwnerId.stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }
}