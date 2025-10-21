package ru.Diana.NauJava;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.Diana.entity.Event;
import ru.Diana.entity.Reminder;
import ru.Diana.entity.User;
import ru.Diana.repository.EventRepository;
import ru.Diana.repository.ReminderRepository;
import ru.Diana.repository.UserRepository;
import ru.Diana.service.EventManagementService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Transactional
class EventManagementServiceTest {

    @Autowired
    private EventManagementService eventManagementService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ReminderRepository reminderRepository;

    @Test
    void testCreateEventWithReminder_Success() {
        String username = "serviceuser_" + UUID.randomUUID().toString().substring(0, 8);
        String eventTitle = "Тестовое событие " + UUID.randomUUID().toString().substring(0, 8);

        User user = new User();
        user.setUsername(username);
        user.setEmail(username + "@example.com");
        user.setPasswordHash("hashed_password");
        user.setTimezone("UTC");
        userRepository.save(user);

        LocalDateTime eventTime = LocalDateTime.now().plusDays(1);
        LocalDateTime remindTime = LocalDateTime.now().plusHours(23);

        eventManagementService.createEventWithReminder(
                eventTitle,
                "Описание тестового события",
                eventTime,
                remindTime,
                username
        );

        List<Event> events = eventRepository.findByUserName(username);
        Assertions.assertFalse(events.isEmpty());

        Event createdEvent = events.stream()
                .filter(e -> e.getTitle().equals(eventTitle))
                .findFirst()
                .orElseThrow();

        Assertions.assertEquals(eventTitle, createdEvent.getTitle());
        Assertions.assertEquals(username, createdEvent.getUser().getUsername());

        List<Reminder> reminders = reminderRepository.findByEventId(createdEvent.getId());
        Assertions.assertFalse(reminders.isEmpty());

        Reminder createdReminder = reminders.get(0);

        Assertions.assertEquals(remindTime.truncatedTo(ChronoUnit.MILLIS),
                createdReminder.getRemindTime().truncatedTo(ChronoUnit.MILLIS));
        Assertions.assertEquals("PUSH", createdReminder.getMethod());
        Assertions.assertFalse(createdReminder.getSent());
    }

    @Test
    void testCreateEventWithReminder_Rollback() {
        String username = "rollbackuser_" + UUID.randomUUID().toString().substring(0, 8);
        String eventTitle = "Событие с ошибкой " + UUID.randomUUID().toString().substring(0, 8);

        User user = new User();
        user.setUsername(username);
        user.setEmail(username + "@example.com");
        user.setPasswordHash("hashed_password");
        user.setTimezone("UTC");
        userRepository.save(user);

        List<Event> initialEvents = eventRepository.findByUserName(username);
        int initialCount = initialEvents.size();

        LocalDateTime eventTime = LocalDateTime.now().plusDays(1);
        LocalDateTime remindTime = eventTime.plusDays(1);

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            eventManagementService.createEventWithReminder(
                    eventTitle,
                    "Описание события с ошибкой",
                    eventTime,
                    remindTime,
                    username
            );
        });

        List<Event> finalEvents = eventRepository.findByUserName(username);
        int finalCount = finalEvents.size();

        boolean eventFound = finalEvents.stream()
                .anyMatch(e -> e.getTitle().equals(eventTitle));

        if (eventFound) {
            System.out.println("ПРОБЛЕМА С ТРАНЗАКЦИЕЙ: Событие '" + eventTitle + "' найдено в базе!");
            System.out.println("Начальное количество событий: " + initialCount);
            System.out.println("Конечное количество событий: " + finalCount);
        }

        Assertions.assertEquals(initialCount, finalCount,
                "Количество событий не должно измениться после отката транзакции");

        Assertions.assertFalse(eventFound,
                "Событие не должно было быть создано из-за отката транзакции");

        Assertions.assertTrue(exception.getMessage().contains("откат транзакции") ||
                        exception.getMessage().contains("Время напоминания не может быть позже времени события"),
                "Сообщение об ошибке должно содержать информацию об откате транзакции");
    }

    @Test
    void testCreateEventWithReminder_UserNotFound() {
        String nonExistentUsername = "nonexistent_" + UUID.randomUUID().toString().substring(0, 8);

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            eventManagementService.createEventWithReminder(
                    "Событие",
                    "Описание",
                    LocalDateTime.now().plusDays(1),
                    LocalDateTime.now().plusHours(23),
                    nonExistentUsername
            );
        });

        Assertions.assertTrue(exception.getMessage().contains("Пользователь не найден"));
    }
}