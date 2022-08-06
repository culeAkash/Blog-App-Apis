package com.blog.app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blog.app.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

	@Query("select c from Category c where c.categoryTitle like :key OR c.categoryDescription like :key OR c.categoryId like :key")
	List<Category> searchCategoriesByKeyword(@Param("key") String key);
}
