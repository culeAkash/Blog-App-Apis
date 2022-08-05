package com.blog.app.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.app.entities.Category;
import com.blog.app.entities.Post;
import com.blog.app.entities.User;

public interface PostRepository extends JpaRepository<Post, Integer> {

	Page<Post> findByCategory(Category category, Pageable pageable);

	Page<Post> findByUser(User user, Pageable pageable);

	// here used convention to define methods for which implemenation will
	// automatically be provided by SPring Boot
}
