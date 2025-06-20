package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.model.AccessError;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.exception.model.ValidationException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDtoForCreate;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemServiceImplTest {

    @InjectMocks
    private ItemServiceImpl itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;

    private final Long userId = 1L;
    private final Long itemId = 2L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        itemService = new ItemServiceImpl(itemRepository, userRepository, bookingRepository, commentRepository);
    }

    @Test
    void createItem_whenUserExists_thenItemIsSavedAndReturned() {
        long userId = 1L;

        ItemDto inputDto = new ItemDto(0L, "Drill", "Electric drill", 0L, 0L, true, null);
        Item item = ItemMapper.toItem(inputDto);
        item.setId(10L);
        User user = new User(userId, "John", "john@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        when(commentRepository.findAllByItemId(item.getId())).thenReturn(List.of());

        ItemDto result = itemService.createItem(inputDto, userId);

        assertNotNull(result);
        assertEquals("Drill", result.getName());
        assertEquals("Electric drill", result.getDescription());
        assertTrue(result.getAvailable());

        verify(userRepository).findById(userId);
        verify(itemRepository).save(any(Item.class));
        verify(commentRepository).findAllByItemId(item.getId());
    }

    @Test
    void createItem_whenUserNotFound_thenThrowsNotFoundException() {
        long userId = 999L;
        ItemDto inputDto = new ItemDto(0L, "Hammer", "Heavy hammer", 0L, 0L, true, null);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(
                ru.practicum.shareit.exception.model.NotFoundException.class,
                () -> itemService.createItem(inputDto, userId)
        );

        assertTrue(exception.getMessage().contains("Пользователя с id = " + userId));
        verify(userRepository).findById(userId);
        verifyNoInteractions(itemRepository);
    }

    @Test
    void createItem_whenValid_shouldReturnItemDto() {
        ItemDto itemDto = new ItemDto(1L, "дрель", "мощная", 1L, 0L, true, null);
        Item item = ItemMapper.toItem(itemDto);
        item.setId(itemId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(itemRepository.save(any())).thenReturn(item);
        when(commentRepository.findAllByItemId(itemId)).thenReturn(Collections.emptyList());

        ItemDto result = itemService.createItem(itemDto, userId);

        assertNotNull(result);
        assertEquals("дрель", result.getName());
    }

    @Test
    void getItemDtoById_whenItemExistsAndUserIsOwner_thenReturnsItemDtoForOwner() {
        long itemId = 1L;
        long ownerId = 10L;

        Item item = new Item(itemId, "Drill", "Powerful drill", ownerId, 0L, true);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(commentRepository.findAllByItemId(itemId)).thenReturn(List.of());
        when(bookingRepository.findFirstOneByItemIdAndStatusAndEndBeforeOrderByEndDesc(eq(itemId), any(), any()))
                .thenReturn(Optional.empty());
        when(bookingRepository.findFirstOneByItemIdAndStatusAndStartAfterOrderByStartAsc(eq(itemId), any(), any()))
                .thenReturn(Optional.empty());

        var result = itemService.getItemDtoById(itemId, ownerId);

        assertNotNull(result);
        assertEquals("Drill", result.getName());
        verify(itemRepository).findById(itemId);
        verify(commentRepository).findAllByItemId(itemId);
    }

    @Test
    void getItemDtoById_whenNotFound_shouldThrowException() {
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> itemService.getItemDtoById(itemId, userId));

        assertTrue(exception.getMessage().contains("не найдено"));
    }

    @Test
    void getAllItemBySearch_shouldReturnEmptyList_whenTextIsEmpty() {
        String text = "";

        Collection<ItemDto> result = itemService.getAllItemBySearch(text);

        assertThat(result).isEmpty();
        verifyNoInteractions(itemRepository, commentRepository);
    }



    @Test
    void updateItem_whenOwnerIsCorrect_thenItemIsUpdated() {
        long ownerId = 5L;
        long itemId = 2L;

        Item existingItem = new Item(itemId, "Old", "Old desc", ownerId, 0L, true);
        ItemDto updateDto = new ItemDto(itemId, "New", "New desc", ownerId, 0L, false, List.of());

        when(userRepository.findById(ownerId)).thenReturn(Optional.of(new User()));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(existingItem));
        when(commentRepository.findAllByItemId(itemId)).thenReturn(List.of());

        ItemDto result = itemService.updateItem(updateDto, ownerId, itemId);

        assertEquals("New", result.getName());
        assertEquals("New desc", result.getDescription());
        assertFalse(result.getAvailable());
        verify(itemRepository).updateItem(itemId, "New", "New desc", false);
    }

    @Test
    void updateItem_whenNotOwner_shouldThrowAccessError() {
        ItemDto itemDto = new ItemDto(itemId, "новое имя", "описание", 1L, 0L, true, null);
        Item existingItem = new Item(itemId, "старое", "старое", 99L, null, true);

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(existingItem));

        assertThrows(AccessError.class, () -> itemService.updateItem(itemDto, userId, itemId));
    }

    @Test
    void createNewComment_whenUserHadBooking_thenCommentSaved() {
        long authorId = 3L;
        long itemId = 4L;
        CommentDtoForCreate commentCreateDto = new CommentDtoForCreate("Nice!");

        Item item = new Item(itemId, "Item", "Desc", 10L, 0L, true);
        User author = new User(authorId, "Alice", "a@example.com");

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(userRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(bookingRepository.existsByItemIdAndBookerIdAndStatusAndEndBefore(eq(itemId), eq(authorId),
                eq(BookingStatus.APPROVED), any()))
                .thenReturn(true);

        Comment savedComment = new Comment(1L, "Nice!", author, item, LocalDateTime.now());
        when(commentRepository.save(any())).thenReturn(savedComment);

        var result = itemService.createNewComment(commentCreateDto, authorId, itemId);

        assertEquals("Nice!", result.getText());
        verify(commentRepository).save(any());
    }

    @Test
    void createNewComment_whenNoBooking_shouldThrowValidationException() {
        Item item = new Item(itemId, "название", "описание", userId, null, true);
        User author = new User(userId, "user", "user@mail.com");

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(userRepository.findById(userId)).thenReturn(Optional.of(author));
        when(bookingRepository.existsByItemIdAndBookerIdAndStatusAndEndBefore(
                eq(itemId), eq(userId), eq(BookingStatus.APPROVED), any(LocalDateTime.class))
        ).thenReturn(false);

        CommentDtoForCreate commentDtoForCreate = new CommentDtoForCreate("отлично");

        assertThrows(ValidationException.class,
                () -> itemService.createNewComment(commentDtoForCreate, userId, itemId));
    }

    @Test
    void createNewComment_shouldThrowNotFound_whenItemNotFound() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> itemService.createNewComment(new CommentDtoForCreate("text"), 1L, 99L));
        assertThat(ex.getMessage()).contains("Вещи с id = 99");
    }

    @Test
    void createNewComment_shouldThrowNotFound_whenUserNotFound() {
        long itemId = 1L;
        Item item = new Item(itemId, "Item", "Desc", 3L, null, true);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> itemService.createNewComment(new CommentDtoForCreate("text"), 999L, itemId));
        assertThat(ex.getMessage()).contains("Пользователя с id = 999");
    }

    @Test
    void deleteItemById_whenOwnerIsCorrect_thenItemDeleted() {
        long itemId = 99L;
        long ownerId = 1L;

        Item item = new Item(itemId, "Del", "To delete", ownerId, 0L, true);

        when(userRepository.findById(ownerId)).thenReturn(Optional.of(new User()));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(commentRepository.findAllByItemId(itemId)).thenReturn(List.of());

        ItemDto result = itemService.deleteItemById(itemId, ownerId);

        assertEquals(itemId, result.getId());
        verify(itemRepository).deleteById(itemId);
    }

    @Test
    void deleteItemById_shouldThrowAccessError_whenUserIsNotOwner() {
        long itemId = 1L;
        long ownerId = 100L;
        long otherUserId = 200L;

        Item item = new Item(itemId, "Вещь", "Описание", ownerId, null, true);

        when(userRepository.findById(otherUserId)).thenReturn(Optional.of(new User()));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        AccessError ex = assertThrows(AccessError.class, () -> itemService.deleteItemById(itemId, otherUserId));

        assertThat(ex.getMessage()).isEqualTo("Удалять данные о вещи может только владелец.");
        verify(itemRepository, never()).deleteById(anyLong());
    }

    @Test
    void deleteAllItemsFromOwner_shouldThrowNotFound_whenUserDoesNotExist() {
        long ownerId = 999L;
        when(userRepository.findById(ownerId)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> itemService.deleteAllItemsFromOwner(ownerId));

        assertThat(ex.getMessage()).contains("Пользователя с id = 999 для отображения не найдено");
        verify(itemRepository, never()).deleteAllItemsFromOwner(anyLong());
    }
}