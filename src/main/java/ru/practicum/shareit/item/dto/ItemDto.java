package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */

@Data
@AllArgsConstructor
public class ItemDto {
    private String name;
    private String description;
    private boolean isAvailable;
    private long requestId;
}
