package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    private long id;

    @FutureOrPresent
    private LocalDateTime start;

    @FutureOrPresent
    private LocalDateTime end;

    private ItemDto item;
    private UserDto booker;
    private BookingStatus status;
}
