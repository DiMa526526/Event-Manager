package ru.Diana.service;

import ru.Diana.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    void createEvent(Long id, String title, String description, LocalDateTime eventTime);
    Event findById(Long id);
    void deleteById(Long id);
    void updateEvent(Long id, String newTitle, String newDesc, LocalDateTime newEventTime);
    List<Event> getAllEvents();
    void markAsNotified(Long id);
}
