package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.exception.model.AccessError;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.request.dao.RequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoForCreate;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RequestServiceImplTest {

    @Mock
    private RequestRepository requestRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private RequestServiceImpl requestService;

    private User user;
    private ItemRequest itemRequest;
    private ItemRequestDtoForCreate requestDtoForCreate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);

        itemRequest = new ItemRequest();
        itemRequest.setId(10L);
        itemRequest.setRequesterId(user.getId());
        itemRequest.setDescription("Test description");

        requestDtoForCreate = new ItemRequestDtoForCreate();
        requestDtoForCreate.setDescription("Test description");
    }

    @Test
    void createItemRequest_UserExists_ReturnsCreatedRequest() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(requestRepository.save(any(ItemRequest.class))).thenAnswer(invocation -> {
            ItemRequest arg = invocation.getArgument(0);
            arg.setId(10L);
            return arg;
        });

        ItemRequestDto result = requestService.createItemRequest(requestDtoForCreate, user.getId());

        assertNotNull(result);
        assertEquals("Test description", result.getDescription());
        verify(requestRepository).save(any(ItemRequest.class));
    }

    @Test
    void createItemRequest_UserNotFound_ThrowsNotFoundException() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> requestService.createItemRequest(requestDtoForCreate, user.getId()));

        assertTrue(ex.getMessage().contains("Пользователя с id ="));
    }

    @Test
    void getItemRequestById_RequestExists_ReturnsDto() {
        when(requestRepository.findById(itemRequest.getId())).thenReturn(Optional.of(itemRequest));
        when(itemRepository.findAllByRequestId(itemRequest.getId())).thenReturn(List.of());

        ItemRequestDto result = requestService.getItemRequestById(itemRequest.getId());

        assertNotNull(result);
        assertEquals(itemRequest.getDescription(), result.getDescription());
    }

    @Test
    void getItemRequestById_RequestNotFound_ThrowsNotFoundException() {
        when(requestRepository.findById(itemRequest.getId())).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> requestService.getItemRequestById(itemRequest.getId()));

        assertTrue(ex.getMessage().contains("Запроса с id ="));
    }


    @Test
    void updateItemRequest_ByOwner_UpdatesAndReturnsDto() {
        when(requestRepository.findById(itemRequest.getId())).thenReturn(Optional.of(itemRequest));
        when(itemRepository.findAllByRequestId(itemRequest.getId())).thenReturn(List.of());

        ItemRequestDtoForCreate updateDto = new ItemRequestDtoForCreate();
        updateDto.setDescription("Updated description");

        doNothing().when(requestRepository).updateItemRequest(itemRequest.getId(), "Updated description");

        ItemRequestDto updated = requestService.updateItemRequest(updateDto, user.getId(), itemRequest.getId());

        assertEquals("Updated description", updated.getDescription());
        verify(requestRepository).updateItemRequest(itemRequest.getId(), "Updated description");
    }

    @Test
    void updateItemRequest_NotOwner_ThrowsAccessError() {
        when(requestRepository.findById(itemRequest.getId())).thenReturn(Optional.of(itemRequest));

        ItemRequestDtoForCreate updateDto = new ItemRequestDtoForCreate();
        updateDto.setDescription("Updated description");

        long notOwnerId = 999L;

        AccessError error = assertThrows(AccessError.class,
                () -> requestService.updateItemRequest(updateDto, notOwnerId, itemRequest.getId()));

        assertTrue(error.getMessage().contains("У вас нет прав доступа"));
    }

    @Test
    void deleteItemRequest_ByOwner_DeletesAndReturnsDto() {
        when(requestRepository.findById(itemRequest.getId())).thenReturn(Optional.of(itemRequest));
        when(itemRepository.findAllByRequestId(itemRequest.getId())).thenReturn(List.of());

        doNothing().when(requestRepository).deleteById(itemRequest.getId());

        ItemRequestDto deletedDto = requestService.deleteItemRequest(itemRequest.getId(), user.getId());

        assertEquals(itemRequest.getDescription(), deletedDto.getDescription());
        verify(requestRepository).deleteById(itemRequest.getId());
    }

    @Test
    void deleteItemRequest_NotOwner_ThrowsAccessError() {
        when(requestRepository.findById(itemRequest.getId())).thenReturn(Optional.of(itemRequest));

        long notOwnerId = 999L;

        AccessError error = assertThrows(AccessError.class,
                () -> requestService.deleteItemRequest(itemRequest.getId(), notOwnerId));

        assertTrue(error.getMessage().contains("У вас нет прав доступа"));
    }


   /* @Test
    void getAllItemRequest_ReturnsAllRequestsWithItems() {
        ItemRequest req1 = new ItemRequest();
        req1.setId(1L);
        req1.setDescription("Request 1");

        ItemRequest req2 = new ItemRequest();
        req2.setId(2L);
        req2.setDescription("Request 2");

        List<ItemRequest> allRequests = List.of(req1, req2);

        Item itemForReq1 = new Item();
        itemForReq1.setId(100L);
        itemForReq1.setRequestId(1L);

        Item itemForReq2 = new Item();
        itemForReq2.setId(101L);
        itemForReq2.setRequestId(2L);

        List<Item> allItems = List.of(itemForReq1, itemForReq2);

        when(requestRepository.findAll()).thenReturn(allRequests);
        when(itemRepository.findAllByRequestIdIn(List.of(1L, 2L))).thenReturn(allItems);

        var results = requestService.getAllItemRequest();

        assertNotNull(results);
        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(dto -> dto.getDescription().equals("Request 1")));
        assertTrue(results.stream().anyMatch(dto -> dto.getDescription().equals("Request 2")));
    }*/

   /* @Test
    void getAllItemRequestsFromRequester_UserExists_ReturnsRequests() {
        long requesterId = user.getId();

        ItemRequest req1 = new ItemRequest();
        req1.setId(1L);
        req1.setRequesterId(requesterId);
        req1.setDescription("Request from user");

        List<ItemRequest> userRequests = List.of(req1);

        Item itemForReq1 = new Item();
        itemForReq1.setId(100L);
        itemForReq1.setRequestId(1L);

        List<Item> allItems = new ArrayList<>(List.of(itemForReq1));

        when(userRepository.findById(requesterId)).thenReturn(Optional.of(user));
        when(requestRepository.findAllByRequesterId(requesterId)).thenReturn(userRequests);
        when(itemRepository.findAllByRequestIdIn(List.of(1L))).thenReturn(allItems);

        var results = requestService.getAllItemRequestsFromRequester(requesterId);

        assertNotNull(results);
        assertEquals(1, results.size());
    }*/

    @Test
    void getAllItemRequestsFromRequester_UserNotFound_ThrowsNotFoundException() {
        long nonExistentUserId = 999L;
        when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> requestService.getAllItemRequestsFromRequester(nonExistentUserId));

        assertTrue(ex.getMessage().contains("Пользователя с id ="));
    }


    /*@Test
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
    }*/

   /* @Test
    void deleteItemRequest_whenUserNotRequester_thenThrowsException() {
        when(requestRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> requestService.deleteItemRequest(1L, 1L));
    }*/

    @Test
    void deleteItemRequest_whenItemRequestNotFound_thenThrowsException() {
        when(requestRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> requestService.deleteItemRequest(1L, 1L));
    }

    @Test
    void updateItemRequest_shouldUpdateItemRequestDescriptionByOwner() {
        ItemRequest req1 = new ItemRequest();
        req1.setId(10L);
        req1.setDescription("Request 1");
        req1.setRequesterId(1L);

        Mockito.when(requestRepository.findById(10L)).thenReturn(Optional.of(req1));
        Mockito.when(requestRepository.findAll()).thenReturn(List.of(
                new ItemRequest(10L, "Новые данные", 1L, null)));

        ItemRequestDtoForCreate forCreate = new ItemRequestDtoForCreate("Новые данные");

        requestService.updateItemRequest(forCreate, 1L, 10L);
        Collection<ItemRequestDto> allItemRequest = requestService.getAllItemRequest();
        Optional<ItemRequestDto> first = allItemRequest.stream().filter(req -> req.getId() == 10L).findFirst();

        assertNotNull(allItemRequest);
        assertTrue(first.isPresent());
        assertEquals(first.get().getDescription(), "Новые данные");
    }

    @Test
    void updateItemRequest_shouldThrowAccessError_whenUserIsNotOwner() {
        ItemRequest req1 = new ItemRequest();
        req1.setId(1L);
        req1.setDescription("Request 1");
        req1.setRequesterId(1L);

        Mockito.when(requestRepository.findById(1L)).thenReturn(Optional.of(req1));

        ItemRequestDtoForCreate forCreate = new ItemRequestDtoForCreate("Новые данные");

        AccessError ex = assertThrows(AccessError.class, () -> {
            requestService.updateItemRequest(forCreate, 200L, 1L);
        });

        assertEquals("У вас нет прав доступа к редактированию этого запроса.", ex.getMessage());
        Mockito.verify(requestRepository, Mockito.never()).updateItemRequest(Mockito.anyLong(), Mockito.any());
    }
}