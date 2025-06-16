package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDtoForCreate;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.item.comment.CommentRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

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
}