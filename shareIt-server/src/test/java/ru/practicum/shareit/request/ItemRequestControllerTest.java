package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoForCreate;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ItemRequestControllerTest {

    private RequestService requestService;
    private ItemRequestController controller;

    private final String headerName = "X-Sharer-User-Id";

    @BeforeEach
    void setup() {
        requestService = mock(RequestService.class);
        controller = new ItemRequestController(requestService);
    }

    @Test
    void createItemRequest_ShouldCallServiceWithCorrectParams() {
        ItemRequestDtoForCreate dtoForCreate = new ItemRequestDtoForCreate("desc");
        long userId = 42L;

        ItemRequestDto expectedResponse = new ItemRequestDto();
        when(requestService.createItemRequest(dtoForCreate, userId)).thenReturn(expectedResponse);

        ItemRequestDto result = controller.createItemRequest(dtoForCreate, userId);

        verify(requestService, times(1)).createItemRequest(dtoForCreate, userId);
        assertThat(result).isEqualTo(expectedResponse);
    }

    @Test
    void getAllItemRequest_WithRequesterId_ShouldCallService() {
        long requesterId = 7L;
        List<ItemRequestDto> expected = List.of(new ItemRequestDto());
        when(requestService.getAllItemRequestsFromRequester(requesterId)).thenReturn(expected);

        var result = controller.getAllItemRequest(requesterId);

        verify(requestService).getAllItemRequestsFromRequester(requesterId);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void getAllItemRequest_WithoutParams_ShouldCallService() {
        List<ItemRequestDto> expected = List.of(new ItemRequestDto());
        when(requestService.getAllItemRequest()).thenReturn(expected);

        var result = controller.getAllItemRequest();

        verify(requestService).getAllItemRequest();
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void getItemRequestById_ShouldCallService() {
        long itemRequestId = 11L;
        ItemRequestDto expected = new ItemRequestDto();
        when(requestService.getItemRequestById(itemRequestId)).thenReturn(expected);

        var result = controller.getItemRequestById(itemRequestId);

        verify(requestService).getItemRequestById(itemRequestId);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void getAllItemRequest_NoRequests_ShouldReturnEmptyList() {
        long requesterId = 123L;
        when(requestService.getAllItemRequestsFromRequester(requesterId)).thenReturn(Collections.emptyList());

        var result = controller.getAllItemRequest(requesterId);

        assertThat(result).isEmpty();
    }

    @Test
    void updateItemRequest_BlankDescription_ShouldThrow() {
        ItemRequestDtoForCreate dto = new ItemRequestDtoForCreate("  ");
        long userId = 1L;
        long itemReqId = 2L;

        when(requestService.updateItemRequest(dto, userId, itemReqId))
                .thenThrow(new IllegalArgumentException("Description must not be blank"));

        assertThrows(IllegalArgumentException.class, () -> controller.updateItemRequest(dto, userId, itemReqId));
    }

    @Test
    void updateItemRequest_NegativeUserId_ShouldThrow() {
        ItemRequestDtoForCreate dto = new ItemRequestDtoForCreate("valid description");
        long userId = -10L;
        long itemReqId = 2L;

        when(requestService.updateItemRequest(dto, userId, itemReqId))
                .thenThrow(new IllegalArgumentException("User id must be positive"));

        assertThrows(IllegalArgumentException.class, () -> controller.updateItemRequest(dto, userId, itemReqId));
    }

    @Test
    void updateItemRequest_ShouldCallService() {
        ItemRequestDtoForCreate dtoForUpdate = new ItemRequestDtoForCreate("updated desc");
        long userId = 15L;
        long itemReqId = 8L;
        ItemRequestDto expected = new ItemRequestDto();

        when(requestService.updateItemRequest(dtoForUpdate, userId, itemReqId)).thenReturn(expected);

        var result = controller.updateItemRequest(dtoForUpdate, userId, itemReqId);

        verify(requestService).updateItemRequest(dtoForUpdate, userId, itemReqId);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void deleteItemRequest_ShouldCallService() {
        long itemRequestIdForDelete = 99L;
        long userId = 3L;
        ItemRequestDto expected = new ItemRequestDto();

        when(requestService.deleteItemRequest(itemRequestIdForDelete, userId)).thenReturn(expected);

        var result = controller.deleteItemRequest(itemRequestIdForDelete, userId);

        verify(requestService).deleteItemRequest(itemRequestIdForDelete, userId);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void updateItemRequest_WithInvalidUserId_ShouldThrowException() {
        ItemRequestDtoForCreate dtoForUpdate = new ItemRequestDtoForCreate("updated desc");
        long userId = -1L;
        long itemReqId = 8L;

        when(requestService.updateItemRequest(dtoForUpdate, userId, itemReqId))
                .thenThrow(new IllegalArgumentException("Invalid user id"));

        assertThrows(IllegalArgumentException.class, () -> {
            controller.updateItemRequest(dtoForUpdate, userId, itemReqId);
        });
    }

    @Test
    void deleteItemRequest_WithMissingUserId_ShouldThrowException() {
        long itemRequestIdForDelete = 99L;
        long userId = 0L; // например, отсутствует в заголовке

        when(requestService.deleteItemRequest(itemRequestIdForDelete, userId))
                .thenThrow(new IllegalArgumentException("User ID missing"));

        assertThrows(IllegalArgumentException.class, () -> {
            controller.deleteItemRequest(itemRequestIdForDelete, userId);
        });
    }
}