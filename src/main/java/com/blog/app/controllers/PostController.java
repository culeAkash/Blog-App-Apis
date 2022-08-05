package com.blog.app.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blog.app.payloads.ApiResponse;
import com.blog.app.payloads.PaginatedResponse;
import com.blog.app.payloads.PostDto;
import com.blog.app.services.PostService;

@RestController
@RequestMapping("/api/")
public class PostController {

	@Autowired
	private PostService postService;

	// create user
	@PostMapping("/user/{userId}/category/{categoryId}/posts")
	public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDtoObject, @PathVariable Integer userId,
			@PathVariable Integer categoryId) {
		PostDto createdPost = this.postService.createPost(postDtoObject, userId, categoryId);
		return new ResponseEntity<PostDto>(createdPost, HttpStatus.CREATED);
	}
	// We take userid and catid from thr url and based on that send to service which
	// will construct Post and send to repository

	// controller for get posts for category
	@GetMapping("/category/{categoryId}/posts")
	// To implement pagination we have to get page size and page number from url
	public ResponseEntity<PaginatedResponse<PostDto>> getAllPostsByCategory(@PathVariable Integer categoryId,
			@RequestParam(value = "pageNumber", defaultValue = "1", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {

		PaginatedResponse<PostDto> postsByCategory = this.postService.getPostsByCategory(categoryId, pageNumber - 1,
				pageSize);
		return new ResponseEntity<>(postsByCategory, HttpStatus.OK);
	}

	// controller for get posts for user
	@GetMapping("/user/{userId}/posts")
	public ResponseEntity<PaginatedResponse<PostDto>> getAllPostsByUser(@PathVariable Integer userId,
			@RequestParam(value = "pageNumber", defaultValue = "1", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {

		PaginatedResponse<PostDto> postsByUser = this.postService.getAllPostsByUser(userId, pageNumber - 1, pageSize);
		return new ResponseEntity<>(postsByUser, HttpStatus.OK);
	}

	// controller to get all posts
	// URL => localhost:8080/api/posts?pageNUmber=2&pageSize=4
	@GetMapping("/posts")
	public ResponseEntity<PaginatedResponse<PostDto>> getAllPosts(
			@RequestParam(value = "pageNumber", defaultValue = "1", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
		// for pagination we will get pageNumber and pageSize from url
		// page number always start from zero so whatever comes from url, we pass to
		// function after decrementing by 1

		PaginatedResponse<PostDto> allPosts = this.postService.getAllPosts(pageNumber - 1, pageSize);
		return new ResponseEntity<PaginatedResponse<PostDto>>(allPosts, HttpStatus.OK);
	}

	// controller to get all posts for a given id
	@GetMapping("/posts/{postId}")
	public ResponseEntity<PostDto> getAllPosts(@PathVariable Integer postId) {
		PostDto postById = this.postService.getPostById(postId);
		return new ResponseEntity<PostDto>(postById, HttpStatus.OK);
	}

	// controller to update post
	@PutMapping("/posts/{postId}")
	public ResponseEntity<PostDto> updatePost(@Valid @RequestBody PostDto postDto, @PathVariable Integer postId) {
		PostDto updatedPost = this.postService.updatePost(postDto, postId);
		return new ResponseEntity<PostDto>(updatedPost, HttpStatus.OK);
	}

	@DeleteMapping("/posts/{postId}")
	public ResponseEntity<ApiResponse> deletePost(@PathVariable Integer postId) {
		this.postService.deletePost(postId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Post has been deleted successfully", true),
				HttpStatus.OK);
	}
}
