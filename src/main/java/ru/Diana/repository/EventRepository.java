package ru.Diana.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.Diana.entity.Event;

import java.util.List;

@Repository
public interface EventRepository extends CrudRepository<Event, Long> {
    

    @Query("SELECT e FROM Event e WHERE e.user.username = :username")
    List<Event> findByUserName(String username);
}