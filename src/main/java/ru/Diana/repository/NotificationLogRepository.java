package ru.Diana.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.Diana.entity.NotificationLog;

@Repository
public interface NotificationLogRepository extends CrudRepository<NotificationLog, Long> {
}