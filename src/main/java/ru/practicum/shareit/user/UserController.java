package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto createNewUser(@Valid @RequestBody UserDto userDto) {
        return userService.createUser(userDto);
    }

    @GetMapping("/{idOfUser}")
    public UserDto getUserById(@PathVariable long idOfUser) {
        return userService.getUserByID(idOfUser);
    }

    @GetMapping
    public Collection<UserDto> getUserById() {
        return userService.getAllUsers();
    }

    @PatchMapping("/{idOfUser}")
    public UserDto updateUser(@RequestBody UserDto userDto, @PathVariable long idOfUser) {
        userDto.setId(idOfUser);
        return userService.updateUser(userDto);
    }

    @DeleteMapping("/{idOfUser}")
    public UserDto deleteUserById(@PathVariable long idOfUser) {
        return userService.deleteUser(idOfUser);
    }
}
