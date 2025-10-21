package ru.Diana.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import ru.Diana.entity.Event;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class EventRepositoryImpl implements EventRepositoryCustom {

    private final EntityManager entityManager;

    @Autowired
    public EventRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Event> findByEventTimeBetweenAndNotified(LocalDateTime startTime, LocalDateTime endTime, Boolean notified) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> criteriaQuery = criteriaBuilder.createQuery(Event.class);
        Root<Event> eventRoot = criteriaQuery.from(Event.class);

        // Условия: eventTime BETWEEN ? AND ? AND notified = ?
        Predicate timeBetweenPredicate = criteriaBuilder.between(eventRoot.get("eventTime"), startTime, endTime);
        Predicate notifiedPredicate = criteriaBuilder.equal(eventRoot.get("notified"), notified);
        Predicate andPredicate = criteriaBuilder.and(timeBetweenPredicate, notifiedPredicate);

        criteriaQuery.select(eventRoot).where(andPredicate);

        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
