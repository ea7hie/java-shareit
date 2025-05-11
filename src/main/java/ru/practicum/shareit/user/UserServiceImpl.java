package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.Collection;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto userDto) {
        return UserMapper.toUserDto(userRepository.createUser(UserMapper.toUser(userDto)));
    }

    @Override
    public UserDto getUserByID(long userDtoId) {
        return UserMapper.toUserDto(userRepository.getUserByID(userDtoId));
    }

    @Override
    public Collection<UserDto> getAllUsers() {
        return userRepository.getAllUsers().stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        return UserMapper.toUserDto(userRepository.updateUser(userDto));
    }

    @Override
    public UserDto deleteUser(long userDtoId) {
        return UserMapper.toUserDto(userRepository.deleteUser(userDtoId));
    }
}
