package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.UserDto;

@Service
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }


    public ResponseEntity<Object> createUser(UserDto userDto) {
        return post("", userDto);
    }

    public ResponseEntity<Object> getUserByID(long idOfUser) {
        return get("/" + idOfUser);
    }

    public ResponseEntity<Object> getAllUsers(long userIdWhoWantView) {
        return get("", userIdWhoWantView);
    }

    public ResponseEntity<Object> updateUser(UserDto userDto, long idOfUser) {
        return patch("/" + idOfUser, idOfUser, userDto);
    }

    public ResponseEntity<Object> deleteUser(long idOfUser) {
        return delete("/" + idOfUser, idOfUser);
    }
}
