package ru.Diana.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import ru.Diana.entity.Event;

import java.util.List;

@Repository
@RepositoryRestResource(exported = false)
public interface EventRepository extends CrudRepository<Event, Long> {
    

    @Query("SELECT e FROM Event e WHERE e.user.username = :username")
    List<Event> findByUserName(String username);
}