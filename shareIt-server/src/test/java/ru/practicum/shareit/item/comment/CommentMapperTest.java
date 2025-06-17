package ru.practicum.shareit.item.comment;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentMapperTest {

    @Test
    void toCommentDto_shouldMapFieldsCorrectly() {
        User author = new User();
        author.setName("Иван Иванов");

        Comment comment = new Comment();
        comment.setId(42L);
        comment.setText("Тестовый комментарий");
        comment.setAuthor(author);
        LocalDateTime now = LocalDateTime.now();
        comment.setDatePosting(now);

        CommentDto dto = CommentMapper.toCommentDto(comment);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(comment.getId());
        assertThat(dto.getText()).isEqualTo(comment.getText());
        assertThat(dto.getAuthorName()).isEqualTo(author.getName());
        assertThat(dto.getCreated()).isEqualTo(now);
    }
}