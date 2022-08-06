package com.blog.app.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blog.app.entities.Category;
import com.blog.app.entities.Post;
import com.blog.app.entities.User;

public interface PostRepository extends JpaRepository<Post, Integer> {

	Page<Post> findByCategory(Category category, Pageable pageable);

	Page<Post> findByUser(User user, Pageable pageable);

	// here used convention to define methods for which implemenation will
	// automatically be provided by SPring Boot

	// search posts by keyword presence in postId, title , content , post's user and
	// post's category
	@Query("select p from Post p where p.postTitle like :key OR p.postContent like :key OR p.postId like :key  OR p.user.name like :key OR p.user.about like :key OR p.category.categoryTitle like :key OR p.category.categoryDescription like :key")
	List<Post> searchPostsBykeyword(@Param("key") String key);
}
