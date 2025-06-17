package ru.practicum.shareit.request.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RequestRepositoryTest {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindAllByRequesterId() {
        User user = new User();
        user.setName("User");
        user.setEmail("user@example.com");
        User saved = userRepository.save(user);

        User user2 = new User();
        user2.setName("User2");
        user2.setEmail("user2@example.com");
        User saved2 = userRepository.save(user2);

        ItemRequest request1 = new ItemRequest();
        request1.setRequesterId(saved.getId());
        request1.setDescription("Request 1");
        request1.setCreated(LocalDateTime.now());
        requestRepository.save(request1);

        ItemRequest request2 = new ItemRequest();
        request2.setRequesterId(saved.getId());
        request2.setDescription("Request 2");
       request2.setCreated(LocalDateTime.now());
        requestRepository.save(request2);

        ItemRequest request3 = new ItemRequest();
        request3.setRequesterId(saved2.getId());
        request3.setDescription("Request 3");
        request3.setCreated(LocalDateTime.now());
        requestRepository.save(request3);

        Collection<ItemRequest> requests = requestRepository.findAllByRequesterId(saved.getId());

        assertThat(requests).hasSize(2);
        assertThat(requests).extracting(ItemRequest::getDescription)
                .containsExactlyInAnyOrder("Request 1", "Request 2");
    }

    @Test
    void testUpdateItemRequest() {
        User user = new User();
        user.setName("User");
        user.setEmail("user@example.com");
        User saved = userRepository.save(user);

        ItemRequest request = new ItemRequest();
        request.setRequesterId(saved.getId());
        request.setDescription("Old description");
        request.setCreated(LocalDateTime.now());
        request = requestRepository.save(request);

        requestRepository.updateItemRequest(request.getId(), "New description");

        Optional<ItemRequest> updatedRequest = requestRepository.findById(request.getId());

        assertThat(updatedRequest).isPresent();
    }
}