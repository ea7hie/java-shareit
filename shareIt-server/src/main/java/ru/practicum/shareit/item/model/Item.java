package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "owner_id")
    private Long ownerId;

    @Column(name = "request_id")
    private Long requestId;

    @Column(name = "is_available")
    private boolean isAvailable;
}