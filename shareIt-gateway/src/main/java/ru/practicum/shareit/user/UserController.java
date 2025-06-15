package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserClient userClient;
    private final String headerOfUserId = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> createNewUser(@RequestBody @Valid UserDto userDto) {
        return userClient.createUser(userDto);
    }

    @GetMapping("/{idOfUser}")
    public ResponseEntity<Object> getUserById(@Positive @PathVariable long idOfUser,
                                              @RequestHeader(headerOfUserId) long userIdWhoWantView) {
        return userClient.getUserByID(idOfUser, userIdWhoWantView);
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
