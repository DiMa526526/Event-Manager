package ru.Diana.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.Diana.entity.Reminder;

import java.util.List;

@Repository
public interface ReminderRepository extends CrudRepository<Reminder, Long> {
    List<Reminder> findByEventId(Long eventId);
}