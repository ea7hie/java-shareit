package ru.practicum.shareit.user.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void existsByEmail_shouldReturnTrue_whenUserWithEmailExists() {
        User user = new User(null, "Alice", "alice@example.com");
        userRepository.save(user);

        boolean exists = userRepository.existsByEmail("alice@example.com");

        assertThat(exists).isTrue();
    }

    @Test
    void existsByEmail_shouldReturnFalse_whenNoUserWithEmail() {
        boolean exists = userRepository.existsByEmail("nonexistent@example.com");
        assertThat(exists).isFalse();
    }

    @Test
    void findById_shouldReturnUser_whenUserExists() {
        User user = new User(null, "Charlie", "charlie@example.com");
        User savedUser = userRepository.save(user);

        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("Charlie");
        assertThat(foundUser.get().getEmail()).isEqualTo("charlie@example.com");
    }

    @Test
    void saveAndFindAll_shouldReturnAllSavedUsers() {
        userRepository.save(new User(null, "User1", "user1@example.com"));
        userRepository.save(new User(null, "User2", "user2@example.com"));

        var users = userRepository.findAll();

        assertThat(users).hasSize(2)
                .extracting(User::getEmail)
                .containsExactlyInAnyOrder("user1@example.com", "user2@example.com");
    }
}