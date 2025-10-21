package ru.Diana.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import ru.Diana.entity.User;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final EntityManager entityManager;

    @Autowired
    public UserRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<User> findByUsernameOrEmail(String username, String email) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> userRoot = criteriaQuery.from(User.class);

        Predicate usernamePredicate = criteriaBuilder.equal(userRoot.get("username"), username);
        Predicate emailPredicate = criteriaBuilder.equal(userRoot.get("email"), email);
        Predicate orPredicate = criteriaBuilder.or(usernamePredicate, emailPredicate);

        criteriaQuery.select(userRoot).where(orPredicate);

        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}