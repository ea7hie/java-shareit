package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private UserMapper mapper;
    private final Map<Long, User> allUsersById = new HashMap<>();
    private long id = 0;

    @Override
    public User createUser(User user) {
        user.setId(getNewId());
        allUsersById.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUserByID(long userId) {
        return getUserOrThrow(userId, "отображения");
    }

    @Override
    public Collection<User> getAllUsers() {
        return allUsersById.values();
    }

    @Override
    public User updateUser(UserDto userDto) {
        User userForUpdate = getUserOrThrow(userDto.getId(), "обновления");

        userForUpdate.setName(userDto.getName() == null ? userForUpdate.getName() : userDto.getName());
        userForUpdate.setEmail(userDto.getEmail() == null ? userForUpdate.getEmail() : userDto.getEmail());

        return userForUpdate;
    }

    @Override
    public User deleteUser(long userId) {
        getUserOrThrow(userId, "удаления");
        return allUsersById.remove(userId);
    }

    private long getNewId() {
        return ++id;
    }

    private User getUserOrThrow(long userId, String message) {
        Optional<User> optionalUser = Optional.ofNullable(allUsersById.get(userId));
        if (optionalUser.isEmpty()) {
            throw new NotFoundException(String.format("Пользователя с id = %d для %s не найдено", userId, message));
        }
        return optionalUser.get();
    }
}
