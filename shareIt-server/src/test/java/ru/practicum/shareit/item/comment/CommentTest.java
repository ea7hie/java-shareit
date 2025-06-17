package ru.practicum.shareit.item.comment;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class CommentTest {
    private static Validator validator;

    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testGettersAndSetters() {
        Comment comment = new Comment();

        comment.setId(10L);
        comment.setText("Test comment");

        User author = new User();
        author.setId(1L);
        author.setName("John Doe");
        comment.setAuthor(author);

        Item item = new Item();
        item.setId(5L);
        item.setName("Drill");
        comment.setItem(item);

        LocalDateTime now = LocalDateTime.now();
        comment.setDatePosting(now);

        assertThat(comment.getId()).isEqualTo(10L);
        assertThat(comment.getText()).isEqualTo("Test comment");
        assertThat(comment.getAuthor()).isEqualTo(author);
        assertThat(comment.getItem()).isEqualTo(item);
        assertThat(comment.getDatePosting()).isEqualTo(now);
    }

    @Test
    void whenTextIsBlank_thenValidationFails() {
        Comment comment = new Comment();
        comment.setText("");

        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);

        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).contains("не должно быть пустым");
    }

    @Test
    void whenTextIsNotBlank_thenValidationPasses() {
        Comment comment = new Comment();
        comment.setText("Nice item");

        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);

        assertThat(violations).isEmpty();
    }


    @Test
    void testAllArgsConstructor() {
        User author = new User(1L, "John", "john@example.com");
        Item item = new Item(2L, "Hammer", "Good hammer", author.getId(), null, true);

        LocalDateTime now = LocalDateTime.now();

        Comment comment = new Comment(3L, "Great!", author, item, now);

        assertThat(comment.getId()).isEqualTo(3L);
        assertThat(comment.getText()).isEqualTo("Great!");
        assertThat(comment.getAuthor()).isEqualTo(author);
        assertThat(comment.getItem()).isEqualTo(item);
        assertThat(comment.getDatePosting()).isEqualTo(now);
    }
}