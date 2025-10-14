package ru.Diana.console;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.Diana.model.Event;
import ru.Diana.service.EventService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Класс для обработки команд, введённых пользователем в консоли.
 */
@Component
public class CommandProcessor {

    private final EventService eventService;

    @Autowired
    public CommandProcessor(EventService eventService) {
        this.eventService = eventService;
    }

    public void processCommand(String input) {
        String[] cmd = input.split(" ", 2); // разделяем команду и аргументы

        switch (cmd[0].toLowerCase()) {
            case "create" -> handleCreate(cmd.length > 1 ? cmd[1] : "");
            case "read" -> handleRead(cmd.length > 1 ? cmd[1] : "");
            case "update" -> handleUpdate(cmd.length > 1 ? cmd[1] : "");
            case "delete" -> handleDelete(cmd.length > 1 ? cmd[1] : "");
            case "list" -> handleList();
            case "notify" -> handleNotify(cmd.length > 1 ? cmd[1] : "");
            default -> System.out.println("⚠️ Неизвестная команда. Доступные команды: create, read, update, delete, list, notify");
        }
    }

    private void handleCreate(String args) {
        try {
            String[] parts = args.split(";");
            Long id = Long.valueOf(parts[0].trim());
            String title = parts[1].trim();
            String desc = parts[2].trim();
            LocalDateTime time = LocalDateTime.parse(parts[3].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            eventService.createEvent(id, title, desc, time);
        } catch (Exception e) {
            System.out.println("Ошибка: используйте формат -> create id; title; description; yyyy-MM-dd HH:mm");
        }
    }

    private void handleRead(String args) {
        try {
            Long id = Long.valueOf(args.trim());
            Event event = eventService.findById(id);
            if (event != null)
                System.out.println(event);
            else
                System.out.println("Событие с id=" + id + " не найдено.");
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

            eventService.updateEvent(id, title, desc, time);
        } catch (Exception e) {
            System.out.println("Ошибка: используйте формат -> update id; title; description; yyyy-MM-dd HH:mm");
        }
    }

    private void handleDelete(String args) {
        try {
            Long id = Long.valueOf(args.trim());
            eventService.deleteById(id);
        } catch (Exception e) {
            System.out.println("Ошибка: используйте формат -> delete id");
        }
    }

    private void handleList() {
        System.out.println("Все события:");
        eventService.getAllEvents().forEach(System.out::println);
    }

    private void handleNotify(String args) {
        try {
            Long id = Long.valueOf(args.trim());
            eventService.markAsNotified(id);
        } catch (Exception e) {
            System.out.println("Ошибка: используйте формат -> notify id");
        }
    }
}
