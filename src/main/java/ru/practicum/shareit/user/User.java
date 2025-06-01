package ru.practicum.shareit.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */

@Data
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    private long id;

    @NotBlank(message = "Имя пользователя не может быть пустым")
    @Column(name = "name", nullable = false)
    private String name;

    @Email(message = "Email пользователя не соответствует формату")
    @Column(name = "email", nullable = false)
    private String email;
}
