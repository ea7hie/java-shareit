package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.enums.Actions;
import ru.practicum.shareit.exception.model.IsNotUniqueEmailException;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> allUsersById = new HashMap<>();
    private long id = 0;

    private final String messageDontUniqueEmailInCreating = "Используйте другой email для регистрации.";
    private final String messageDontUniqueEmailInUpdating = "Используйте другой email для обновления текущего email.";

    @Override
    public User createUser(User user) {
        if (isEmailUsedAlready(user.getEmail())) {
            throw new IsNotUniqueEmailException(messageDontUniqueEmailInCreating);
        }
        user.setId(getNewId());
        allUsersById.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUserByID(long userId) {
        return getUserOrThrow(userId, Actions.TO_VIEW);
    }

    @Override
    public Collection<User> getAllUsers() {
        return allUsersById.values();
    }

    @Override
    public User updateUser(UserDto userDto) {
        User userForUpdate = getUserOrThrow(userDto.getId(), Actions.TO_UPDATE);

        if (isEmailUsedAlready(userDto.getEmail())) {
            throw new IsNotUniqueEmailException(messageDontUniqueEmailInUpdating);
        }

        userForUpdate.setName(userDto.getName() == null ? userForUpdate.getName() : userDto.getName());
        userForUpdate.setEmail(userDto.getEmail() == null ? userForUpdate.getEmail() : userDto.getEmail());

        return allUsersById.put(userForUpdate.getId(), userForUpdate);
    }

    @Override
    public User deleteUser(long userId) {
        getUserOrThrow(userId, Actions.TO_DELETE);
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

    private boolean isEmailUsedAlready(String emailForCheck) {
        return allUsersById.values().stream()
                .map(User::getEmail)
                .toList()
                .contains(emailForCheck);
    }
}
