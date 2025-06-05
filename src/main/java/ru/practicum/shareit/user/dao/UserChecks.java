package ru.practicum.shareit.user.dao;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.enums.Actions;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.user.User;

import java.util.Optional;

@UtilityClass
public class UserChecks {
    public void isUserExistsById(UserRepository userRepository, long userIdForCheck) {
        Optional<User> optionalUser = userRepository.findById(userIdForCheck);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException(String.format("Пользователя с id = %d для %s не найдено", userIdForCheck,
                    Actions.TO_VIEW));
        }
    }

    public boolean isEmailUsedAlready(UserRepository userRepository, String emailForCheck) {
        return userRepository.existsByEmail(emailForCheck);
    }

    public User getUserOrThrow(UserRepository userRepository, long userId, String message) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException(String.format("Пользователя с id = %d для %s не найдено", userId, message));
        }
        return optionalUser.get();
    }
}
