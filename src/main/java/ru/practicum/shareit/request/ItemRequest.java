package ru.practicum.shareit.request;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */

@Data
@AllArgsConstructor
@Entity
@Table(name = "requests")
public class ItemRequest {
    @Id
    private long id;

    @NotBlank
    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "requester_id")
    private long requesterId;

    @NotNull
    @Column(name = "date_creation")
    private LocalDateTime created;
}