package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */

@Data
@AllArgsConstructor
public class User {
    private long id;

    @NotBlank(message = "Имя пользователя не может быть пустым")
    private String name;

    @Email(message = "Email пользователя не соответствует формату")
    private String email;
}
