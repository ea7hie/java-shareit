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
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.RequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoForCreate;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserRepository;

import java.util.ArrayList;
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

    @Test
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
        itemForReq1.setName("name");
        itemForReq1.setDescription("desc");
        itemForReq1.setOwnerId(1L);

        List<Item> allItems = new ArrayList<>(List.of(itemForReq1));

        when(userRepository.findById(requesterId)).thenReturn(Optional.of(user));
        when(requestRepository.findAllByRequesterId(requesterId)).thenReturn(userRequests);
        when(itemRepository.findAllByRequestIdIn(List.of(1L))).thenReturn(allItems);

        var results = requestService.getAllItemRequestsFromRequester(requesterId);

        assertNotNull(results);
        assertEquals(1, results.size());
    }

    @Test
    void getAllItemRequestsFromRequester_UserNotFound_ThrowsNotFoundException() {
        long nonExistentUserId = 999L;
        when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> requestService.getAllItemRequestsFromRequester(nonExistentUserId));

        assertTrue(ex.getMessage().contains("Пользователя с id ="));
    }

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