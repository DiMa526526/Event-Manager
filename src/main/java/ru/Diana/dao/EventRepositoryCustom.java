package ru.Diana.dao;

import ru.Diana.entity.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepositoryCustom {
    List<Event> findByEventTimeBetweenAndNotified(LocalDateTime startTime, LocalDateTime endTime, Boolean notified);
}
