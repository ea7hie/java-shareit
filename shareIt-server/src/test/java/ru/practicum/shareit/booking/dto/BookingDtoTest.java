package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingDtoTest {
    private final JacksonTester<BookingDto> json;

    @Test
    void testBookItemRequestDto() throws Exception {
        ItemDto item = new ItemDto();
        UserDto user = new UserDto();

        LocalDateTime now = LocalDateTime.now();
        BookingDto bookItemRequestDto = new BookingDto(1L, now, now.plusDays(2), item, user, BookingStatus.WAITING);

        JsonContent<BookingDto> result = json.write(bookItemRequestDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
    }
}