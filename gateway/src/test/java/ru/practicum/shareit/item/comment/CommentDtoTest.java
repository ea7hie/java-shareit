package ru.practicum.shareit.item.comment;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommentDtoTest {

    @Test
    void testConstructorAndGetters() {
        LocalDateTime now = LocalDateTime.now();
        CommentDto comment = new CommentDto(1L, "Great item!", "Alice", now);

        assertEquals(1L, comment.getId());
        assertEquals("Great item!", comment.getText());
        assertEquals("Alice", comment.getAuthorName());
        assertEquals(now, comment.getCreated());
    }

    @Test
    void testSetters() {
        CommentDto comment = new CommentDto(0L, null, null, null);

        comment.setId(2L);
        comment.setText("Nice one");
        comment.setAuthorName("Bob");
        LocalDateTime created = LocalDateTime.now();
        comment.setCreated(created);

        assertEquals(2L, comment.getId());
        assertEquals("Nice one", comment.getText());
        assertEquals("Bob", comment.getAuthorName());
        assertEquals(created, comment.getCreated());
    }

    @Test
    void testEqualsAndHashCode() {
        LocalDateTime time = LocalDateTime.now();
        CommentDto comment1 = new CommentDto(3L, "Wow!", "Charlie", time);
        CommentDto comment2 = new CommentDto(3L, "Wow!", "Charlie", time);

        assertEquals(comment1, comment2);
        assertEquals(comment1.hashCode(), comment2.hashCode());
    }

    @Test
    void testToString() {
        LocalDateTime now = LocalDateTime.of(2023, 1, 1, 12, 0);
        CommentDto comment = new CommentDto(4L, "Cool", "Diana", now);
        String expected = "CommentDto(id=4, text=Cool, authorName=Diana, created=2023-01-01T12:00)";
        assertEquals(expected, comment.toString());
    }
}