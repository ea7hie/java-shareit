package ru.practicum.shareit.item.comment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserRepository;

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

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Should find all comments by itemId")
    void findAllByItemId() {
        User user = new User();
        user.setName("User");
        user.setEmail("user@example.com");
        User saved = userRepository.save(user);

        Item item1 = new Item();
        item1.setName("Item 1");
        item1.setDescription("Item 1 desc");
        item1.setOwnerId(saved.getId());
        item1.setRequestId(0L);
        item1.setAvailable(true);

        Item item2 = new Item();
        item2.setName("Item 2");
        item2.setDescription("Item 2 desc");
        item2.setOwnerId(saved.getId());
        item2.setRequestId(0L);
        item2.setAvailable(true);

        Item item3 = new Item();
        item3.setName("Item 3");
        item3.setDescription("Item 3 desc");
        item3.setOwnerId(saved.getId());
        item3.setRequestId(0L);
        item3.setAvailable(true);

        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);

        Comment comment1 = new Comment(0L, "Text1", user, item1, LocalDateTime.now());
        Comment comment2 = new Comment(0L, "Text2", user, item2, LocalDateTime.now());
        Comment comment3 = new Comment(0L, "Text3", user, item3, LocalDateTime.now());
        Comment comment4 = new Comment(0L, "Text4", user, item2, LocalDateTime.now());
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);
        commentRepository.save(comment4);

        Collection<Comment> comments = commentRepository.findAllByItemId(item2.getId());

        assertThat(comments).hasSize(2)
                .extracting(Comment::getText)
                .containsExactlyInAnyOrder("Text2", "Text4");
    }

    @Test
    @DisplayName("Should find all comments by collection of itemIds")
    void findAllByItemIdIn() {
        User user = new User();
        user.setName("User");
        user.setEmail("user2@example.com");
        User saved = userRepository.save(user);

        Item item1 = new Item();
        item1.setName("Item 1");
        item1.setDescription("Item 1 desc");
        item1.setOwnerId(saved.getId());
        item1.setRequestId(0L);
        item1.setAvailable(true);

        Item item2 = new Item();
        item2.setName("Item 2");
        item2.setDescription("Item 2 desc");
        item2.setOwnerId(saved.getId());
        item2.setRequestId(0L);
        item2.setAvailable(true);

        Item item3 = new Item();
        item3.setName("Item 3");
        item3.setDescription("Item 3 desc");
        item3.setOwnerId(saved.getId());
        item3.setRequestId(0L);
        item3.setAvailable(true);

        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);

        Comment comment1 = new Comment(0L, "Text1", user, item1, LocalDateTime.now());
        Comment comment2 = new Comment(0L, "Text2", user, item2, LocalDateTime.now());
        Comment comment3 = new Comment(0L, "Text3", user, item3, LocalDateTime.now());
        Comment comment4 = new Comment(0L, "Text4", user, item2, LocalDateTime.now());
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);
        commentRepository.save(comment4);

        Collection<Comment> comments = commentRepository.findAllByItemIdIn(List.of(item1.getId(), item3.getId()));

        assertThat(comments).hasSize(2)
                .extracting(Comment::getText)
                .containsExactlyInAnyOrder("Text1", "Text3");
    }
}