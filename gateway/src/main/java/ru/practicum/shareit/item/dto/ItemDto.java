package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.comment.CommentDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    private long ownerId;
    private long requestId;
    private Boolean available;

    private List<CommentDto> comments;
}
