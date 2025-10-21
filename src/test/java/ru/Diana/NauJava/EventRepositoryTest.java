package ru.Diana.NauJava;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.Diana.entity.Event;
import ru.Diana.entity.User;
import ru.Diana.repository.EventRepository;
import ru.Diana.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@SpringBootTest
class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindEventsByUserName() {
        String username = "eventuser_" + UUID.randomUUID().toString().substring(0, 8);
        User user = new User();
        user.setUsername(username);
        user.setEmail(username + "@example.com");
        user.setPasswordHash("hashed_password");
        user.setTimezone("UTC");
        User savedUser = userRepository.save(user);

        Event event = new Event();
        event.setTitle("Тестовое событие");
        event.setDescription("Описание тестового события");
        event.setEventTime(LocalDateTime.now().plusDays(1));
        event.setNotified(false);
        event.setUser(savedUser);

        eventRepository.save(event);

        List<Event> foundEvents = eventRepository.findByUserName(username);

        Assertions.assertFalse(foundEvents.isEmpty());
        Assertions.assertEquals("Тестовое событие", foundEvents.get(0).getTitle());
        Assertions.assertEquals(username, foundEvents.get(0).getUser().getUsername());
    }
}