package ru.Diana.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import ru.Diana.entity.Reminder;

import java.util.List;

@Repository
@RepositoryRestResource(exported = false)
public interface ReminderRepository extends CrudRepository<Reminder, Long> {
    List<Reminder> findByEventId(Long eventId);
}