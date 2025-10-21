package ru.Diana.NauJava;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.Diana.entity.User;
import ru.Diana.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindUserByUsername() {
        String username = "testuser_" + UUID.randomUUID().toString().substring(0, 8);

        User user = new User();
        user.setUsername(username);
        user.setEmail(username + "@example.com");
        user.setPasswordHash("hashed_password");
        user.setTimezone("UTC");

        User savedUser = userRepository.save(user);

        Optional<User> foundUser = userRepository.findByUsername(username);

        Assertions.assertTrue(foundUser.isPresent());
        Assertions.assertEquals(savedUser.getId(), foundUser.get().getId());
        Assertions.assertEquals(username, foundUser.get().getUsername());
    }

    @Test
    void testFindUserByUsernameOrEmail() {
        String username = "testuser_" + UUID.randomUUID().toString().substring(0, 8);
        String email = "test_" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash("hashed_password");
        user.setTimezone("Europe/Moscow");

        userRepository.save(user);

        List<User> usersByUsername = userRepository.findByUsernameOrEmail(username, "nonexistent@example.com");
        Assertions.assertFalse(usersByUsername.isEmpty());
        Assertions.assertEquals(username, usersByUsername.get(0).getUsername());

        List<User> usersByEmail = userRepository.findByUsernameOrEmail("nonexistent", email);
        Assertions.assertFalse(usersByEmail.isEmpty());
        Assertions.assertEquals(email, usersByEmail.get(0).getEmail());
    }
}