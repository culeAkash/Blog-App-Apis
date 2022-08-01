package com.blog.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.app.entities.Post;

public interface PostRepository extends JpaRepository<Post, Integer> {

}
