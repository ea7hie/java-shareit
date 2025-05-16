package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserRepository {
    User createUser(User user);

    User getUserByID(long userId);

    Collection<User> getAllUsers();

    User updateUser(UserDto userDto);

    User deleteUser(long userId);
}
