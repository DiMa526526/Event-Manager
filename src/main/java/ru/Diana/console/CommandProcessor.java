package ru.Diana.console;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.Diana.entity.Event;
import ru.Diana.entity.User;
import ru.Diana.repository.EventRepository;
import ru.Diana.repository.UserRepository;
import ru.Diana.service.EventManagementService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;


@Component
public class CommandProcessor {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventManagementService eventManagementService;

    @Autowired
    public CommandProcessor(EventRepository eventRepository,
                            UserRepository userRepository,
                            EventManagementService eventManagementService) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.eventManagementService = eventManagementService;
    }

    public void processCommand(String input) {
        String[] cmd = input.split(" ", 2);

        switch (cmd[0].toLowerCase()) {
            case "create" -> handleCreate(cmd.length > 1 ? cmd[1] : "");
            case "read" -> handleRead(cmd.length > 1 ? cmd[1] : "");
            case "update" -> handleUpdate(cmd.length > 1 ? cmd[1] : "");
            case "delete" -> handleDelete(cmd.length > 1 ? cmd[1] : "");
            case "list" -> handleList();
            case "notify" -> handleNotify(cmd.length > 1 ? cmd[1] : "");
            case "create-event" -> handleCreateEvent(cmd.length > 1 ? cmd[1] : "");
            case "test-data" -> handleTestData();
            default -> System.out.println("Неизвестная команда. Доступные команды: create, read, update, delete, list, notify, create-event, test-data");
        }
    }

    private void handleCreate(String args) {
        try {
            String[] parts = args.split(";");
            String title = parts[0].trim();
            String desc = parts[1].trim();
            LocalDateTime time = LocalDateTime.parse(parts[2].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            String username = parts[3].trim();

            Optional<User> existingUser = userRepository.findByUsername(username);
            User user;
            if (existingUser.isPresent()) {
                user = existingUser.get();
            } else {
                user = new User();
                user.setUsername(username);
                user.setEmail(username + "@example.com");
                user.setPasswordHash("default_hash");
                user.setTimezone("UTC");
                user = userRepository.save(user);
            }

            Event event = new Event();
            event.setTitle(title);
            event.setDescription(desc);
            event.setEventTime(time);
            event.setNotified(false);
            event.setUser(user);

            eventRepository.save(event);
            System.out.println("Событие создано: " + title);

        } catch (Exception e) {
            System.out.println("Ошибка: используйте формат -> create title; description; yyyy-MM-dd HH:mm; username");
            System.out.println("Пример: create Встреча; Совещание; 2024-01-15 14:30; john_doe");
        }
    }

    private void handleRead(String args) {
        try {
            Long id = Long.valueOf(args.trim());
            Optional<Event> event = eventRepository.findById(id);
            if (event.isPresent()) {
                Event e = event.get();
                System.out.println("Событие: " + e.getTitle());
                System.out.println("Описание: " + e.getDescription());
                System.out.println("Время: " + e.getEventTime());
                System.out.println("Уведомлено: " + e.getNotified());
                System.out.println("Пользователь: " + e.getUser().getUsername());
            } else {
                System.out.println("Событие с id=" + id + " не найдено.");
            }
        } catch (Exception e) {
            System.out.println("Ошибка: используйте формат -> read id");
        }
    }

    private void handleUpdate(String args) {
        try {
            String[] parts = args.split(";");
            Long id = Long.valueOf(parts[0].trim());
            String title = parts[1].trim();
            String desc = parts[2].trim();
            LocalDateTime time = LocalDateTime.parse(parts[3].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            Optional<Event> eventOpt = eventRepository.findById(id);
            if (eventOpt.isPresent()) {
                Event event = eventOpt.get();
                event.setTitle(title);
                event.setDescription(desc);
                event.setEventTime(time);
                eventRepository.save(event);
                System.out.println("Событие обновлено: " + title);
            } else {
                System.out.println("Событие с id=" + id + " не найдено.");
            }
        } catch (Exception e) {
            System.out.println("Ошибка: используйте формат -> update id; title; description; yyyy-MM-dd HH:mm");
        }
    }

    private void handleDelete(String args) {
        try {
            Long id = Long.valueOf(args.trim());
            if (eventRepository.existsById(id)) {
                eventRepository.deleteById(id);
                System.out.println("Событие с id=" + id + " удалено.");
            } else {
                System.out.println("Событие с id=" + id + " не найдено.");
            }
        } catch (Exception e) {
            System.out.println("Ошибка: используйте формат -> delete id");
        }
    }

    private void handleList() {
        System.out.println("📋 Все события:");
        List<Event> events = (List<Event>) eventRepository.findAll();
        if (events.isEmpty()) {
            System.out.println("   Нет событий");
        } else {
            events.forEach(event -> {
                System.out.println("   ID: " + event.getId() +
                        " | Title: " + event.getTitle() +
                        " | Time: " + event.getEventTime() +
                        " | User: " + event.getUser().getUsername() +
                        " | Notified: " + event.getNotified());
            });
        }
    }

    private void handleNotify(String args) {
        try {
            Long id = Long.valueOf(args.trim());
            Optional<Event> eventOpt = eventRepository.findById(id);
            if (eventOpt.isPresent()) {
                Event event = eventOpt.get();
                event.setNotified(true);
                eventRepository.save(event);
                System.out.println("Событие отмечено как уведомленное: " + event.getTitle());
            } else {
                System.out.println("Событие с id=" + id + " не найдено.");
            }
        } catch (Exception e) {
            System.out.println("Ошибка: используйте формат -> notify id");
        }
    }

    private void handleCreateEvent(String args) {
        try {
            String[] parts = args.split(";");
            String title = parts[0].trim();
            String desc = parts[1].trim();
            LocalDateTime eventTime = LocalDateTime.parse(parts[2].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            LocalDateTime remindTime = LocalDateTime.parse(parts[3].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            String username = parts[4].trim();

            eventManagementService.createEventWithReminder(title, desc, eventTime, remindTime, username);
            System.out.println("Событие с напоминанием успешно создано!");

        } catch (Exception e) {
            System.out.println("Ошибка: используйте формат -> create-event title; description; yyyy-MM-dd HH:mm; yyyy-MM-dd HH:mm; username");
            System.out.println("Пример: create-event Встреча; Совещание; 2024-01-15 14:30; 2024-01-15 14:00; john_doe");
        }
    }

    private void handleTestData() {
        try {
            User user = new User();
            user.setUsername("test_user");
            user.setEmail("test@example.com");
            user.setPasswordHash("test_hash");
            user.setTimezone("Europe/Moscow");
            userRepository.save(user);

            Event event1 = new Event();
            event1.setTitle("Тестовая встреча");
            event1.setDescription("Первое тестовое событие");
            event1.setEventTime(LocalDateTime.now().plusDays(1));
            event1.setNotified(false);
            event1.setUser(user);
            eventRepository.save(event1);

            Event event2 = new Event();
            event2.setTitle("Важная задача");
            event2.setDescription("Второе тестовое событие");
            event2.setEventTime(LocalDateTime.now().plusDays(2));
            event2.setNotified(true);
            event2.setUser(user);
            eventRepository.save(event2);

            System.out.println("Тестовые данные созданы!");
            System.out.println("Пользователь: test_user");
            System.out.println("Создано событий: " + eventRepository.count());

        } catch (Exception e) {
            System.out.println("Ошибка при создании тестовых данных: " + e.getMessage());
        }
    }
}