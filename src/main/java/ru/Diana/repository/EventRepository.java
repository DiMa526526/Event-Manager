package ru.Diana.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.Diana.model.Event;

import java.util.List;
import java.util.Optional;

@Component
public class EventRepository implements CrudRepository<Event, Long> {

    private final List<Event> eventContainer;

    @Autowired
    public EventRepository(List<Event> eventContainer) {
        this.eventContainer = eventContainer;
    }

    @Override
    public void create(Event event) {
        boolean exists = eventContainer.stream()
                .anyMatch(e -> e.getId().equals(event.getId()));
        if (exists) {
            System.out.println("Ошибка: событие с id=" + event.getId() + " уже существует!");
        } else {
            eventContainer.add(event);
            System.out.println("Создано событие: " + event);
        }
    }

    @Override
    public Event read(Long id) {
        Optional<Event> event = eventContainer.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst();

        return event.orElse(null); // если не найдено — возвращаем null
    }

    @Override
    public void update(Event updatedEvent) {
        for (int i = 0; i < eventContainer.size(); i++) {
            Event current = eventContainer.get(i);
            if (current.getId().equals(updatedEvent.getId())) {
                eventContainer.set(i, updatedEvent);
                System.out.println("Событие обновлено: " + updatedEvent);
                return;
            }
        }
        System.out.println("Не найдено событие с id=" + updatedEvent.getId());
    }

    @Override
    public void delete(Long id) {
        boolean removed = eventContainer.removeIf(e -> e.getId().equals(id));
        if (removed) {
            System.out.println("Событие с id=" + id + " удалено.");
        } else {
            System.out.println("Событие с id=" + id + " не найдено.");
        }
    }

    public List<Event> getAll() {
        return eventContainer;
    }
}
