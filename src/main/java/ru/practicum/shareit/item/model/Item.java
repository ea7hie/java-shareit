package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

/**
 * TODO Sprint add-controllers.
 */

@Data
@AllArgsConstructor
public class Item {
    private long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    private User owner;
    private ItemRequest request;
    private boolean isAvailable;
}
