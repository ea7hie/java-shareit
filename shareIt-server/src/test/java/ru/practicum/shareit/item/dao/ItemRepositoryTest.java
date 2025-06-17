package ru.practicum.shareit.item.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    private Item item1;
    private Item item2;
    private User saved;
    private User saved2;

    @BeforeEach
    void setup() {
        User user = new User();
        user.setName("User");
        user.setEmail("user@example.com");
        saved = userRepository.save(user);

        User user2 = new User();
        user2.setName("User2");
        user2.setEmail("user2@example.com");
        saved2 = userRepository.save(user2);


        item1 = new Item();
        item1.setName("Отвертка");
        item1.setDescription("Красная отвертка");
        item1.setAvailable(true);
        item1.setOwnerId(saved.getId());
        item1.setRequestId(10L);

        item2 = new Item();
        item2.setName("Молоток");
        item2.setDescription("Большой молоток");
        item2.setAvailable(false);
        item2.setOwnerId(saved2.getId());
        item2.setRequestId(20L);

        itemRepository.save(item1);
        itemRepository.save(item2);
    }

    @Test
    void testFindAllByOwnerId() {
        List<Item> items = itemRepository.findAllByOwnerId(saved.getId());
        assertThat(items).hasSize(1).contains(item1);
    }

    @Test
    void testFindByDescriptionContainsIgnoreCaseAndIsAvailableIsTrueOrNameContainsIgnoreCaseAndIsAvailableIsTrue() {
        List<Item> items = itemRepository.findByDescriptionContainsIgnoreCaseAndIsAvailableIsTrueOrNameContainsIgnoreCaseAndIsAvailableIsTrue(
                "отвертка", "молоток"
        );
        assertThat(items).hasSize(1).contains(item1);
    }

    @Test
    void testFindAllByRequestId() {
        List<Item> items = itemRepository.findAllByRequestId(10L);
        assertThat(items).hasSize(1).contains(item1);
    }

    @Test
    void testFindAllByRequestIdIn() {
        List<Item> items = itemRepository.findAllByRequestIdIn(List.of(10L, 20L));
        assertThat(items).hasSize(2).containsExactlyInAnyOrder(item1, item2);
    }

   /* @Test
    void testUpdateItem() {
        itemRepository.updateItem(item1.getId(), "Отвертка 2", "Новая красная отвертка", false);
        Optional<Item> updatedItem = itemRepository.findById(item1.getId());
        assertThat(updatedItem).isPresent();
        assertThat(updatedItem.get().getName()).isEqualTo("Отвертка 2");
        assertThat(updatedItem.get().getDescription()).isEqualTo("Новая красная отвертка");
        assertThat(updatedItem.get().isAvailable()).isFalse();
    }*/

    @Test
    void testDeleteAllItemsFromOwner() {
        itemRepository.deleteAllItemsFromOwner(saved.getId());
        List<Item> items = itemRepository.findAllByOwnerId(saved.getId());
        assertThat(items).isEmpty();
        assertThat(itemRepository.findAllByOwnerId(saved2.getId())).hasSize(1);
    }

    @Test
    void testFindAllByOwnerId_emptyResult() {
        List<Item> items = itemRepository.findAllByOwnerId(999L);
        assertThat(items).isEmpty();
    }

    @Test
    void testFindByDescriptionAndName_caseInsensitive() {
        List<Item> items = itemRepository.findByDescriptionContainsIgnoreCaseAndIsAvailableIsTrueOrNameContainsIgnoreCaseAndIsAvailableIsTrue(
                "красНАЯ", "мОЛоток"
        );
        assertThat(items).hasSize(1).contains(item1);
    }

    @Test
    void testFindByDescriptionAndName_noResults() {
        List<Item> items = itemRepository.findByDescriptionContainsIgnoreCaseAndIsAvailableIsTrueOrNameContainsIgnoreCaseAndIsAvailableIsTrue(
                "неизвестно", "тоже нет"
        );
        assertThat(items).isEmpty();
    }

    @Test
    void testFindAllByRequestId_nonExisting() {
        List<Item> items = itemRepository.findAllByRequestId(999L);
        assertThat(items).isEmpty();
    }

    @Test
    void testFindAllByRequestIdIn_emptyList() {
        List<Item> items = itemRepository.findAllByRequestIdIn(List.of());
        assertThat(items).isEmpty();
    }

    @Test
    void testUpdateItem_notExistingId() {
        itemRepository.updateItem(999L, "Новое имя", "Новое описание", true);
        assertThat(itemRepository.findById(item1.getId())).hasValueSatisfying(item ->
                assertThat(item.getName()).isEqualTo(item1.getName())
        );
    }

    @Test
    void testDeleteAllItemsFromOwner_nonExistingOwner() {
        itemRepository.deleteAllItemsFromOwner(999L);
        assertThat(itemRepository.findAll()).hasSize(2);
    }
}