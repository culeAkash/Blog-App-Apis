package com.blog.app.services;

import java.util.List;

import com.blog.app.payloads.PaginatedResponse;
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
	PaginatedResponse<PostDto> getAllPosts(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

	// get post by id
	PostDto getPostById(Integer postId);

	// get all posts of a category
	PaginatedResponse<PostDto> getPostsByCategory(Integer categoryId, Integer pageNumber, Integer pageSize,
			String sortBy, String sortDir);

	// get all posts by useer
	PaginatedResponse<PostDto> getAllPostsByUser(Integer userId, Integer pageNumber, Integer pageSize, String sortBy,
			String sortDir);

	// search post by keyword
	List<PostDto> getPostsByKeyword(String keyword);
}
