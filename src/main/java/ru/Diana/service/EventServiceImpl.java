package ru.Diana.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.Diana.model.Event;
import ru.Diana.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository)  {
        this.eventRepository = eventRepository;
    }

    @Override
    public void createEvent(Long id, String title, String description, LocalDateTime eventTime) {
        Event newEvent = new Event();
        newEvent.setId(id);
        newEvent.setTitle(title);
        newEvent.setDescription(description);
        newEvent.setEventTime(eventTime);
        newEvent.setNotified(false);

        eventRepository.create(newEvent);
    }

    @Override
    public Event findById(Long id) {
        return eventRepository.read(id);
    }

    @Override
    public void deleteById(Long id) {
        eventRepository.delete(id);
    }

    @Override
    public void updateEvent(Long id, String newTitle, String newDesc, LocalDateTime newEventTime) {
        Event event = eventRepository.read(id);
        if (event != null) {
            event.setTitle(newTitle);
            event.setDescription(newDesc);
            event.setEventTime(newEventTime);
            eventRepository.update(event);
        } else {
            System.out.println("Событие с этим Id не найдено");
        }
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.getAll();
    }

    @Override
    public void markAsNotified(Long id) {
        Event event = eventRepository.read(id);
        if (event != null) {
            event.setNotified(true);
            eventRepository.update(event);
            System.out.println("Событие отмечено как уведомленное");
        } else {
            System.out.println("Событие с этим Id не найдено");
        }
    }
}
