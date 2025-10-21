package ru.Diana.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.Diana.entity.Category;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {
}
