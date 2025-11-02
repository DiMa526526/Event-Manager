package ru.Diana.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.Diana.entity.Event;
import ru.Diana.entity.Reminder;
import ru.Diana.entity.User;
import ru.Diana.entity.Category;
import ru.Diana.exception.ResourceNotFoundException;
import ru.Diana.repository.EventRepository;
import ru.Diana.repository.ReminderRepository;
import ru.Diana.repository.UserRepository;
import ru.Diana.repository.CategoryRepository;
import ru.Diana.dto.UserDTO;
import ru.Diana.dto.EventDTO;
import ru.Diana.dto.ReminderDTO;
import ru.Diana.dto.CategoryDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Custom API", description = "Кастомные операции с событиями, напоминаниями и пользователями")
@RestController
@RequestMapping("/api/custom")
public class CustomRepositoryController {

    private final EventRepository eventRepository;
    private final ReminderRepository reminderRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public CustomRepositoryController(EventRepository eventRepository,
                                      ReminderRepository reminderRepository,
                                      UserRepository userRepository,
                                      CategoryRepository categoryRepository) {
        this.eventRepository = eventRepository;
        this.reminderRepository = reminderRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    private UserDTO toUserDTO(User user) {
        if (user == null) return null;
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getTimezone());
    }

    private EventDTO toEventDTO(Event event) {
        if (event == null) return null;
        return new EventDTO(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getEventTime(),
                event.getNotified(),
                toUserDTO(event.getUser()),
                event.getCategory() != null ? event.getCategory().getId() : null
        );
    }

    private ReminderDTO toReminderDTO(Reminder reminder) {
        if (reminder == null) return null;
        return new ReminderDTO(
                reminder.getId(),
                reminder.getRemindTime(),
                reminder.getMethod(),
                reminder.getSent(),
                toUserDTO(reminder.getUser()),
                reminder.getEvent() != null ? reminder.getEvent().getId() : null
        );
    }

    private CategoryDTO toCategoryDTO(Category category) {
        if (category == null) return null;
        return new CategoryDTO(
                category.getId(),
                category.getName(),
                category.getColor(),
                category.getDescription(),
                toUserDTO(category.getUser())
        );
    }

    @Operation(summary = "Получить события по имени пользователя", description = "Возвращает список всех событий для указанного пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "События найдены"),
            @ApiResponse(responseCode = "404", description = "События не найдены")
    })
    @GetMapping("/events/by-username/{username}")
    public ResponseEntity<List<EventDTO>> getEventsByUsername(@PathVariable String username) {
        List<Event> events = eventRepository.findByUserName(username);
        if (events.isEmpty()) {
            throw new ResourceNotFoundException("Не найдено событий для пользователя с именем: " + username);
        }
        List<EventDTO> dtos = events.stream().map(this::toEventDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Получить напоминания по ID события", description = "Возвращает список всех напоминаний для указанного события")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Напоминания найдены"),
            @ApiResponse(responseCode = "404", description = "Напоминания не найдены")
    })
    @GetMapping("/reminders/by-event/{eventId}")
    public ResponseEntity<List<ReminderDTO>> getRemindersByEventId(@PathVariable Long eventId) {
        List<Reminder> reminders = reminderRepository.findByEventId(eventId);
        if (reminders.isEmpty()) {
            throw new ResourceNotFoundException("Не найдено напоминаний для события с ID: " + eventId);
        }
        List<ReminderDTO> dtos = reminders.stream().map(this::toReminderDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Получить пользователя по имени", description = "Возвращает информацию о пользователе по его имени")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь найден"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/users/by-username/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Не найден пользователь с именем: " + username));
        return ResponseEntity.ok(toUserDTO(user));
    }

    @Operation(summary = "Поиск пользователей по имени или email", description = "Возвращает список пользователей, соответствующих указанному имени или email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователи найдены"),
            @ApiResponse(responseCode = "404", description = "Пользователи не найдены")
    })
    @GetMapping("/users/search")
    public ResponseEntity<List<UserDTO>> getUsersByUsernameOrEmail(@RequestParam String username, @RequestParam String email) {
        List<User> users = userRepository.findByUsernameOrEmail(username, email);
        if (users.isEmpty()) {
            throw new ResourceNotFoundException("Не найдено пользователей с именем: " + username + " или email: " + email);
        }
        List<UserDTO> userDTOs = users.stream().map(this::toUserDTO).collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    @Operation(summary = "Получить пользователя по ID", description = "Возвращает информацию о пользователе по его идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь найден"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Не найден пользователь с ID: " + id));
        return ResponseEntity.ok(toUserDTO(user));
    }

    @Operation(summary = "Получить категории по имени пользователя", description = "Возвращает список категорий для указанного пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Категории найдены"),
            @ApiResponse(responseCode = "404", description = "Категории не найдены")
    })
    @GetMapping("/categories/by-username/{username}")
    public ResponseEntity<List<CategoryDTO>> getCategoriesByUsername(@PathVariable String username) {
        List<Category> categories = categoryRepository.findByUserUsername(username);
        if (categories.isEmpty()) {
            throw new ResourceNotFoundException("Не найдено категорий для пользователя с именем: " + username);
        }
        List<CategoryDTO> dtos = categories.stream().map(this::toCategoryDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}