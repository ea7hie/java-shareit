package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.comment.CommentDtoForCreate;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> createItem(ItemDto itemDto, long userId) {
        itemDto.setOwnerId(userId);
        return post("", userId, itemDto);
    }

    public ResponseEntity<Object> getAllItemsByOwnerId(long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getItemDtoById(long idOfItem, long userId) {
        return get("/" + idOfItem, userId);
    }

    public ResponseEntity<Object> getAllItemBySearch(String text, long userId) {
        Map<String, Object> parameters = Map.of("text", text);
        return get("/search?text={text}", userId, parameters);
    }

    public ResponseEntity<Object> updateItem(ItemDto itemDto, long ownerId, long idOfItem) {
        return patch("/" + idOfItem, ownerId, itemDto);
    }

    public ResponseEntity<Object> deleteItemById(long idOfItem, long ownerId) {
        return delete("/" + idOfItem, ownerId);
    }

    public ResponseEntity<Object> deleteAllItemsFromOwner(long ownerId) {
        return delete("", ownerId);
    }

    public ResponseEntity<Object> createNewComment(CommentDtoForCreate commentDtoForCreate,
                                                   long authorId, long itemId) {
        return post("/" + itemId + "/comment", authorId, commentDtoForCreate);
    }
}
