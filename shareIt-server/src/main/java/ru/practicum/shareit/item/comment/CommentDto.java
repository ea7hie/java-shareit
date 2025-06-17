package ru.practicum.shareit.item.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentDto {
    private long id;
    @NotNull
    @NotBlank
    private String text;
    @NotNull
    @NotBlank
    private String authorName;
    private LocalDateTime created;
}
