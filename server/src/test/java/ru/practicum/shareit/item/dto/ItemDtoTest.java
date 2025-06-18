package ru.practicum.shareit.item.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.comment.CommentDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ItemDtoTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldCreateItemDtoWithAllFields() {
        List<CommentDto> comments = new ArrayList<>();
        comments.add(new CommentDto(1L, "comment text", "author", null));

        ItemDto itemDto = new ItemDto(
                1L,
                "Item name",
                "Item description",
                10L,
                20L,
                true,
                comments
        );

        assertEquals(1L, itemDto.getId());
        assertEquals("Item name", itemDto.getName());
        assertEquals("Item description", itemDto.getDescription());
        assertEquals(10L, itemDto.getOwnerId());
        assertEquals(20L, itemDto.getRequestId());
        assertTrue(itemDto.getAvailable());
        assertEquals(comments, itemDto.getComments());
    }

    @Test
    void shouldFailValidationIfNameIsBlank() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("  ");  // blank
        itemDto.setDescription("Valid description");

        Set<ConstraintViolation<ItemDto>> violations = validator.validate(itemDto);

        assertFalse(violations.isEmpty());

        boolean nameViolationFound = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name"));

        assertTrue(nameViolationFound);
    }

    @Test
    void shouldFailValidationIfDescriptionIsBlank() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Valid name");
        itemDto.setDescription("");  // blank

        Set<ConstraintViolation<ItemDto>> violations = validator.validate(itemDto);

        assertFalse(violations.isEmpty());

        boolean descriptionViolationFound = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("description"));

        assertTrue(descriptionViolationFound);
    }

    @Test
    void shouldPassValidationForValidFields() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Valid name");
        itemDto.setDescription("Valid description");

        Set<ConstraintViolation<ItemDto>> violations = validator.validate(itemDto);

        assertTrue(violations.isEmpty());
    }
}