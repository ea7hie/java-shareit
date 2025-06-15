package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.ItemRequestDtoForCreate;

@Service
public class RequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    @Autowired
    public RequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> createItemRequest(ItemRequestDtoForCreate itemRequestDto, long userId) {
        return post("", userId, itemRequestDto);
    }

    public ResponseEntity<Object> getAllItemRequestsFromRequester(long requesterId) {
        return get("", requesterId);
    }

    public ResponseEntity<Object> getAllItemRequest(long userId) {
        return get("/all", userId);
    }

    public ResponseEntity<Object> getItemRequestById(long itemRequestId, long userId) {
        return get("/" + itemRequestId, userId);
    }

    public ResponseEntity<Object> updateItemRequest(ItemRequestDtoForCreate itemRequestDtoForUpdate,
                                                    long userId, long itemReqId) {
        return patch("/" + itemReqId, userId, itemRequestDtoForUpdate);
    }

    public ResponseEntity<Object> deleteItemRequest(long itemRequestIdForDelete, long userId) {
        return delete("/" + itemRequestIdForDelete, userId);
    }
}
