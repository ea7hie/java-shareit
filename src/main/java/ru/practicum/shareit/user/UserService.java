package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto getUserByID(long userDtoId);

    Collection<UserDto> getAllUsers();

    UserDto updateUser(UserDto userDto);

    UserDto deleteUser(long userDtoId);
}
