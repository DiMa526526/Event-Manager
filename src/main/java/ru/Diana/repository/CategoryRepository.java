package ru.Diana.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import ru.Diana.entity.Category;

@Repository
@RepositoryRestResource
public interface CategoryRepository extends CrudRepository<Category, Long> {
}
