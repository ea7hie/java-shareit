package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentDtoForCreate;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForOwner;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Autowired
    private ObjectMapper objectMapper;

    private final String HEADER = "X-Sharer-User-Id";

    @Test
    void shouldCreateNewItem() throws Exception {
        ItemDto itemDto = new ItemDto(1L, "Drill", "Powerful drill", 1L, 0L,  true, null);
        Mockito.when(itemService.createItem(any(), eq(1L))).thenReturn(itemDto);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HEADER, 1L)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()))
                .andExpect(jsonPath("$.name").value(itemDto.getName()));
    }

    @Test
    void shouldReturnItemsByOwner() throws Exception {
        ItemDtoForOwner item = new ItemDtoForOwner(1L, "Drill", "Powerful drill", true, null, null, List.of());
        Mockito.when(itemService.getAllItemsByOwnerId(1L)).thenReturn(List.of(item));

        mockMvc.perform(get("/items")
                        .header(HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(item.getId()));
    }

    @Test
    void shouldReturnItemById() throws Exception {
        ItemDtoForOwner item = new ItemDtoForOwner(1L, "Drill", "Powerful drill", true, null, null, List.of());
        Mockito.when(itemService.getItemDtoById(1L, 1L)).thenReturn(item);

        mockMvc.perform(get("/items/1")
                        .header(HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(item.getId()));
    }

    @Test
    void shouldSearchItems() throws Exception {
        ItemDto itemDto = new ItemDto(1L, "Drill", "Powerful drill", 1L, 0L,  true, null);
        Mockito.when(itemService.getAllItemBySearch("drill")).thenReturn(List.of(itemDto));

        mockMvc.perform(get("/items/search")
                        .param("text", "drill"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Drill"));
    }

    @Test
    void shouldUpdateItem() throws Exception {
        ItemDto updated = new ItemDto(1L, "Updated Drill", "Now updated", 1L, 0L, true, null);
        Mockito.when(itemService.updateItem(any(), eq(1L), eq(1L))).thenReturn(updated);

        mockMvc.perform(patch("/items/1")
                        .header(HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Drill"));
    }

    @Test
    void shouldDeleteItem() throws Exception {
        ItemDto deleted = new ItemDto(1L, "Drill", "Powerful drill", 1L, 0L,  true, null);
        Mockito.when(itemService.deleteItemById(1L, 1L)).thenReturn(deleted);

        mockMvc.perform(delete("/items/1")
                        .header(HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(deleted.getId()));
    }

    @Test
    void shouldDeleteAllItems() throws Exception {
        ItemDto item = new ItemDto(1L, "Drill", "Powerful drill", 1L, 0L, true, null);
        Mockito.when(itemService.deleteAllItemsFromOwner(1L)).thenReturn(List.of(item));

        mockMvc.perform(delete("/items")
                        .header(HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(item.getId()));
    }

    @Test
    void shouldCreateNewComment() throws Exception {
        CommentDtoForCreate createDto = new CommentDtoForCreate("Nice item!");
        CommentDto response = new CommentDto(1L, "Nice item!", "User", null);

        Mockito.when(itemService.createNewComment(any(), eq(1L), eq(1L))).thenReturn(response);

        mockMvc.perform(post("/items/1/comment")
                        .header(HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Nice item!"));
    }
}