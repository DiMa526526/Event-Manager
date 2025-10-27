package ru.Diana.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.Diana.entity.Event;
import ru.Diana.entity.Reminder;
import ru.Diana.entity.User;
import ru.Diana.exception.ResourceNotFoundException;
import ru.Diana.repository.EventRepository;
import ru.Diana.repository.ReminderRepository;
import ru.Diana.repository.UserRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;
import java.util.Optional;

@Tag(name = "Custom API", description = "Кастомные операции с событиями, напоминаниями и пользователями")
@RestController
@RequestMapping("/api/custom")
public class CustomRepositoryController {

    private final EventRepository eventRepository;
    private final ReminderRepository reminderRepository;
    private final UserRepository userRepository;

    @Autowired
    public CustomRepositoryController(EventRepository eventRepository,
                                      ReminderRepository reminderRepository,
                                      UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.reminderRepository = reminderRepository;
        this.userRepository = userRepository;
    }

    @Operation(
            summary = "Получить события по имени пользователя",
            description = "Возвращает список всех событий для указанного пользователя"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "События найдены"),
            @ApiResponse(responseCode = "404", description = "События не найдены")
    })
    @GetMapping("/events/by-username/{username}")
    public ResponseEntity<List<Event>> getEventsByUsername(
            @Parameter(description = "Имя пользователя", example = "john_doe")
            @PathVariable String username) {
        List<Event> events = eventRepository.findByUserName(username);
        if (events.isEmpty()) {
            throw new ResourceNotFoundException("No events found for username: " + username);
        }
        return ResponseEntity.ok(events);
    }

    @Operation(
            summary = "Получить напоминания по ID события",
            description = "Возвращает список всех напоминаний для указанного события"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Напоминания найдены"),
            @ApiResponse(responseCode = "404", description = "Напоминания не найдены")
    })
    @GetMapping("/reminders/by-event/{eventId}")
    public ResponseEntity<List<Reminder>> getRemindersByEventId(
            @Parameter(description = "ID события", example = "1")
            @PathVariable Long eventId) {
        List<Reminder> reminders = reminderRepository.findByEventId(eventId);
        if (reminders.isEmpty()) {
            throw new ResourceNotFoundException("No reminders found for event ID: " + eventId);
        }
        return ResponseEntity.ok(reminders);
    }

    @Operation(
            summary = "Получить пользователя по имени",
            description = "Возвращает информацию о пользователе по его имени"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь найден"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/users/by-username/{username}")
    public ResponseEntity<User> getUserByUsername(
            @Parameter(description = "Имя пользователя", example = "john_doe")
            @PathVariable String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        return ResponseEntity.ok(user);
    }

    @Operation(
            summary = "Поиск пользователей по имени или email",
            description = "Возвращает список пользователей, соответствующих указанному имени или email"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователи найдены"),
            @ApiResponse(responseCode = "404", description = "Пользователи не найдены")
    })
    @GetMapping("/users/search")
    public ResponseEntity<List<User>> getUsersByUsernameOrEmail(
            @Parameter(description = "Имя пользователя", example = "john_doe")
            @RequestParam String username,

            @Parameter(description = "Email пользователя", example = "john@example.com")
            @RequestParam String email) {
        List<User> users = userRepository.findByUsernameOrEmail(username, email);
        if (users.isEmpty()) {
            throw new ResourceNotFoundException("No users found with username: " + username + " or email: " + email);
        }
        return ResponseEntity.ok(users);
    }

    @Operation(
            summary = "Получить пользователя по ID",
            description = "Возвращает информацию о пользователе по его идентификатору"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь найден"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(
            @Parameter(description = "ID пользователя", example = "1")
            @PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        return ResponseEntity.ok(user);
    }
}