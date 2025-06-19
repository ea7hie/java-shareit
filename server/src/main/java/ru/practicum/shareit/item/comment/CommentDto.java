package ru.practicum.shareit.item.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentDto {
    private long id;

    @NotBlank
    private String text;

    @NotBlank
    private String authorName;
    private LocalDateTime created;
}
