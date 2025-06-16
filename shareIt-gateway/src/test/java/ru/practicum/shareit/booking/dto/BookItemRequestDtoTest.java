package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = BookItemRequestDto.class)
class BookItemRequestDtoTest {
    private final JacksonTester<BookItemRequestDto> json;

    @Test
    void testBookItemRequestDto() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        BookItemRequestDto bookItemRequestDto = new BookItemRequestDto(1L, now, now.plusDays(2));

        JsonContent<BookItemRequestDto> result = json.write(bookItemRequestDto);

        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
    }
}