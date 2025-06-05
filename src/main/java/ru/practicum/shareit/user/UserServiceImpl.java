package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.enums.Actions;
import ru.practicum.shareit.exception.model.IsNotUniqueEmailException;
import ru.practicum.shareit.user.dao.UserChecks;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final String messageDontUniqueEmailInCreating = "Используйте другой email для регистрации.";
    private final String messageDontUniqueEmailInUpdating = "Используйте другой email для обновления текущего email.";

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        if (UserChecks.isEmailUsedAlready(userRepository, userDto.getEmail())) {
            throw new IsNotUniqueEmailException(messageDontUniqueEmailInCreating);
        }
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userDto)));
    }

    @Override
    public UserDto getUserByID(long userDtoId) {
        return UserMapper.toUserDto(UserChecks.getUserOrThrow(userRepository, userDtoId, Actions.TO_VIEW));
    }

    @Override
    public Collection<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    @Override
    @Transactional
    public UserDto updateUser(UserDto userDto, long idOfUser) {
        User userForUpdate = UserChecks.getUserOrThrow(userRepository, idOfUser, Actions.TO_UPDATE);

        if (userDto.getEmail() != null && !userDto.getEmail().equals(userForUpdate.getEmail())) {
            if (UserChecks.isEmailUsedAlready(userRepository, userDto.getEmail())) {
                throw new IsNotUniqueEmailException(messageDontUniqueEmailInUpdating);
            }
            userForUpdate.setEmail(userDto.getEmail());
        }

        userForUpdate.setName(userDto.getName() == null ? userForUpdate.getName() : userDto.getName());

        userRepository.updateUser(idOfUser, userForUpdate.getName(), userForUpdate.getEmail());
        return UserMapper.toUserDto(userForUpdate);
    }

    @Override
    @Transactional
    public UserDto deleteUser(long userDtoId) {
        User userForDelete = UserChecks.getUserOrThrow(userRepository, userDtoId, Actions.TO_DELETE);
        userRepository.deleteById(userDtoId);
        return UserMapper.toUserDto(userForDelete);
    }
}
