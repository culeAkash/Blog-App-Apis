package com.blog.app.controllers;

import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import com.blog.app.payloads.ApiResponse;
import com.blog.app.payloads.PostDto;
import com.blog.app.services.PostService;

@RestController
@RequestMapping("/api/")
public class PostController {

	@Autowired
	private PostService postService;

	// create user
	@PostMapping("/user/{userId}/category/{categoryId}/posts")
	public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDtoObject, @PathVariable Integer userId,
			@PathVariable Integer categoryId) {
		PostDto createdPost = this.postService.createPost(postDtoObject, userId, categoryId);
		return new ResponseEntity<PostDto>(createdPost, HttpStatus.CREATED);
	}
	// We take userid and catid from thr url and based on that send to service which
	// will construct Post and send to repository

	// controller for get posts for category
	@GetMapping("/category/{categoryId}/posts")
	public ResponseEntity<List<PostDto>> getAllPostsByCategory(@PathVariable Integer categoryId) {
		List<PostDto> postsByCategory = this.postService.getPostsByCategory(categoryId);
		return new ResponseEntity<>(postsByCategory, HttpStatus.OK);
	}

	// controller for get posts for user
	@GetMapping("/user/{userId}/posts")
	public ResponseEntity<List<PostDto>> getAllPostsByUser(@PathVariable Integer userId) {
		List<PostDto> postsByUser = this.postService.getAllPostsByUser(userId);
		return new ResponseEntity<>(postsByUser, HttpStatus.OK);
	}

	// controller to get all posts
	@GetMapping("/posts")
	public ResponseEntity<List<PostDto>> getAllPosts() {
		List<PostDto> allPosts = this.postService.getAllPosts();
		return new ResponseEntity<List<PostDto>>(allPosts, HttpStatus.OK);
	}

	// controller to get all posts for a given id
	@GetMapping("/posts/{postId}")
	public ResponseEntity<PostDto> getAllPosts(@PathVariable Integer postId) {
		PostDto postById = this.postService.getPostById(postId);
		return new ResponseEntity<PostDto>(postById, HttpStatus.OK);
	}

	// controller to update post
	@PutMapping("/posts/{postId}")
	public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto, @PathVariable Integer postId) {
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
