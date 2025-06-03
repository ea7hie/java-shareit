package ru.practicum.shareit.user;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.enums.Actions;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.user.dao.UserRepository;

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
}
