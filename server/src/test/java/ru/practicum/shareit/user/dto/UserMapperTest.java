package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.User;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    @Test
    void testToUserDto() {
        User user = new User(1L, "Иван", "ivan@example.com");

        UserDto dto = UserMapper.toUserDto(user);

        assertNotNull(dto);
        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getName(), dto.getName());
        assertEquals(user.getEmail(), dto.getEmail());
    }

    @Test
    void testToUser() {
        UserDto dto = new UserDto(2L, "Пётр", "petr@example.com");

        User user = UserMapper.toUser(dto);

        assertNotNull(user);
        assertEquals(dto.getId(), user.getId());
        assertEquals(dto.getName(), user.getName());
        assertEquals(dto.getEmail(), user.getEmail());
    }

    @Test
    void testToUserDtoWithNullUser() {
        assertThrows(NullPointerException.class, () -> UserMapper.toUserDto(null));
    }

    @Test
    void testToUserWithNullUserDto() {
        assertThrows(NullPointerException.class, () -> UserMapper.toUser(null));
    }
}