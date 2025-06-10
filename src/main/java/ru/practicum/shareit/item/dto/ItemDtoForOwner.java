package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.comment.CommentDto;

import java.util.List;

@Data
@AllArgsConstructor
public class ItemDtoForOwner {
    private long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;
    private Boolean available;

    private BookingDto lastBooking;
    private BookingDto nextBooking;

    private List<CommentDto> comments;
}