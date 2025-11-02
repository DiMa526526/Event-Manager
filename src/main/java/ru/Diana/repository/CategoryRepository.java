package ru.Diana.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import ru.Diana.entity.Category;

import java.util.List;

@Repository
@RepositoryRestResource(exported = false)
public interface CategoryRepository extends CrudRepository<Category, Long> {
    List<Category> findByUserUsername(String username);
}