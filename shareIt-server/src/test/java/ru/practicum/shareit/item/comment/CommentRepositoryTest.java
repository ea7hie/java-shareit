package ru.practicum.shareit.item.comment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    @DisplayName("Should find all comments by itemId")
    void findAllByItemId() {
        User user = new User();
        Item item10 = new Item();
        item10.setId(10L);
        Item item11 = new Item();
        item11.setId(11L);
        Item item12 = new Item();
        item12.setId(12L);

        itemRepository.save(item10);
        itemRepository.save(item11);
        itemRepository.save(item12);

        Comment comment1 = new Comment(1L, "Text1", user, item10, LocalDateTime.now());
        Comment comment2 = new Comment(0L, "Text2", user, item11, LocalDateTime.now());
        Comment comment3 = new Comment(0L, "Text3", user, item12, LocalDateTime.now());
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);

        Collection<Comment> comments = commentRepository.findAllByItemId(1L);

        assertThat(comments).hasSize(2)
                .extracting(Comment::getText)
                .containsExactlyInAnyOrder("Text1", "Text2");
    }

    @Test
    @DisplayName("Should find all comments by collection of itemIds")
    void findAllByItemIdIn() {
        User user = new User();
        Item item10 = new Item();
        item10.setId(10L);
        Item item11 = new Item();
        item11.setId(11L);
        Item item12 = new Item();
        item12.setId(12L);

        Comment comment1 = new Comment(null, "Text1", user, item10, LocalDateTime.now());
        Comment comment2 = new Comment(null, "Text2", user, item11, LocalDateTime.now());
        Comment comment3 = new Comment(null, "Text3", user, item12, LocalDateTime.now());
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);

        Collection<Comment> comments = commentRepository.findAllByItemIdIn(List.of(1L, 3L));

        assertThat(comments).hasSize(2)
                .extracting(Comment::getText)
                .containsExactlyInAnyOrder("Text1", "Text3");
    }
}