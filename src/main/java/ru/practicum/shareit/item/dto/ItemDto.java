package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemDto {
    private long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    private long ownerId;
    private long requestId;
    private Boolean available;
}
