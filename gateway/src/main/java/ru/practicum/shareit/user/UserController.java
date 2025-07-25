package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.UserDto;

@Controller
@Validated
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserClient userClient;
    private final String headerOfUserId = BaseClient.headerOfUserId;

    @PostMapping
    public ResponseEntity<Object> createNewUser(@RequestBody @Valid UserDto userDto) {
        return userClient.createUser(userDto);
    }

    @GetMapping("/{idOfUser}")
    public ResponseEntity<Object> getUserById(@Positive @PathVariable long idOfUser) {
        return userClient.getUserByID(idOfUser);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers(@RequestHeader(headerOfUserId) long userIdWhoWantView) {
        return userClient.getAllUsers(userIdWhoWantView);
    }

    @PatchMapping("/{idOfUser}")
    public ResponseEntity<Object> updateUser(@RequestBody UserDto userDto,
                                             @Positive @PathVariable long idOfUser) {
        return userClient.updateUser(userDto, idOfUser);
    }

    @DeleteMapping("/{idOfUser}")
    public ResponseEntity<Object> deleteUserById(@Positive @PathVariable long idOfUser) {
        return userClient.deleteUser(idOfUser);
    }
}
