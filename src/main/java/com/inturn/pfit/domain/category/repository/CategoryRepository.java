package com.inturn.pfit.domain.category.repository;

import com.inturn.pfit.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>, CategoryRepositoryDsl {

	Optional<Category> findByCategoryOrder(Integer categoryOrder);
}
