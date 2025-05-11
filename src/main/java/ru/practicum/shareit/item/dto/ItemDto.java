package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;

/**
 * TODO Sprint add-controllers.
 */

@Data
@AllArgsConstructor
public class ItemDto {
    private long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    private long ownerId;
    private ItemRequest request;
    private Boolean isAvailable;
}
