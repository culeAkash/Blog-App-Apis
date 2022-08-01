package com.blog.app.services;

import java.util.List;

import com.blog.app.entities.Post;
import com.blog.app.payloads.PostDto;

public interface PostService {

	// create Post
	// We will take user and category id in url
	PostDto createPost(PostDto postDto, Integer userId, Integer categoryId);

	// update
	PostDto updatePost(PostDto postDto, Integer postId);

	// delete
	void deletePost(Integer postId);

	// get posts

	// get all posts
	List<Post> getAllPosts();

	// get post by id
	Post getPostById(Integer postId);

	// get all posts of a category
	List<Post> getPostsByCategory(Integer categoryId);

	// get all posts by useer
	List<Post> getAllPostsByUser(Integer userId);

	// search post by keyword
	List<Post> getPostsByKeyword(String keyword);
}
