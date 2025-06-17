package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.exception.model.IsNotUniqueEmailException;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private final UserDto userDto = new UserDto(1L, "Alice", "alice@example.com");
    private final User user = new User(1L, "Alice", "alice@example.com");

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_whenEmailNotUsed_thenSavesAndReturnsDto() {
        when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user);

        UserDto result = userService.createUser(userDto);

        assertEquals(userDto, result);
        verify(userRepository).save(any());
    }

    @Test
    void createUser_whenEmailAlreadyUsed_thenThrowsException() {
        when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(true);

        assertThrows(IsNotUniqueEmailException.class, () -> userService.createUser(userDto));
        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_whenUserDtoIsNull_thenThrowsException() {
        assertThrows(NullPointerException.class, () -> userService.createUser(null));
    }

    @Test
    void createUser_whenEmailAlreadyUsed_thenThrowsIsNotUniqueEmailException() {
        UserDto userDto = new UserDto(0L, "Bob", "duplicate@example.com");
        when(userRepository.existsByEmail("duplicate@example.com")).thenReturn(true);

        IsNotUniqueEmailException exception = assertThrows(
                IsNotUniqueEmailException.class,
                () -> userService.createUser(userDto)
        );

        assertEquals("Используйте другой email для регистрации.", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void getUserByID_whenUserExists_thenReturnsDto() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDto result = userService.getUserByID(1L);

        assertEquals(userDto, result);
    }

    @Test
    void getUserByID_whenUserNotFound_thenThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getUserByID(1L));
    }

    @Test
    void getAllUsers_thenReturnsListOfDtos() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        Collection<UserDto> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertTrue(result.contains(userDto));
    }

    @Test
    void getAllUsers_whenRepositoryReturnsEmptyList_thenReturnsEmptyList() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        Collection<UserDto> result = userService.getAllUsers();

        assertTrue(result.isEmpty());
    }

    @Test
    void getUserOrThrow_whenUserNotFound_thenThrowsNotFoundExceptionWithMessage() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> userService.getUserByID(99L)
        );

        assertTrue(exception.getMessage().contains("Пользователя с id = 99 для отображения не найдено"));
    }

    @Test
    void updateUser_whenValid_thenUpdatesAndReturnsDto() {
        UserDto updateDto = new UserDto(0L, "UpdatedName", "alice@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail(updateDto.getEmail())).thenReturn(false);

        UserDto result = userService.updateUser(updateDto, 1L);

        assertEquals("UpdatedName", result.getName());
        verify(userRepository).updateUser(eq(1L), eq("UpdatedName"), eq("alice@example.com"));
    }

    @Test
    void updateUser_whenEmailUsed_thenThrowsException() {
        UserDto updateDto = new UserDto(0L, "Name", "new@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail("new@example.com")).thenReturn(true);

        assertThrows(IsNotUniqueEmailException.class, () -> userService.updateUser(updateDto, 1L));
        verify(userRepository, never()).updateUser(anyLong(), anyString(), anyString());
    }

    @Test
    void updateUser_whenNameAndEmailChanged_thenUpdatesBoth() {
        UserDto updateDto = new UserDto(0L, "NewName", "new@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);

        UserDto result = userService.updateUser(updateDto, 1L);

        assertEquals("NewName", result.getName());
        assertEquals("new@example.com", result.getEmail());
        verify(userRepository).updateUser(1L, "NewName", "new@example.com");
    }

    @Test
    void updateUser_whenNameIsNull_thenKeepsOldName() {
        UserDto updateDto = new UserDto(0L, null, "alice@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDto result = userService.updateUser(updateDto, 1L);

        assertEquals("Alice", result.getName()); // имя не изменилось
        verify(userRepository).updateUser(eq(1L), eq("Alice"), eq("alice@example.com"));
    }

    @Test
    void updateUser_whenEmailAlreadyUsed_thenThrowsIsNotUniqueEmailException() {
        UserDto updateDto = new UserDto(0L, "Bob", "existing@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        IsNotUniqueEmailException exception = assertThrows(
                IsNotUniqueEmailException.class,
                () -> userService.updateUser(updateDto, 1L)
        );

        assertEquals("Используйте другой email для обновления текущего email.", exception.getMessage());
        verify(userRepository, never()).updateUser(anyLong(), any(), any());
    }

    @Test
    void deleteUser_whenUserExists_thenDeletesAndReturnsDto() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDto result = userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
        assertEquals(userDto, result);
    }

    @Test
    void deleteUser_whenUserNotFound_thenThrowsNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> userService.deleteUser(1L)
        );

        assertTrue(exception.getMessage().contains("Пользователя с id = 1 для удаления не найдено"));
        verify(userRepository, never()).deleteById(anyLong());
    }

    @Test
    void deleteUser_whenUserNotFound_thenThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.deleteUser(1L));
    }
}