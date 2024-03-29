package com.blog.app.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.hibernate.engine.jdbc.StreamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.multipart.MultipartFile;

import com.blog.app.payloads.ApiResponse;
import com.blog.app.payloads.FileResponse;
import com.blog.app.payloads.PaginatedResponse;
import com.blog.app.payloads.PostDto;
import com.blog.app.services.FileService;
import com.blog.app.services.PostService;
import com.blog.app.utils.ApplicationConstants;

@RestController
@RequestMapping("/api/v1")
public class PostController {

	@Autowired
	private PostService postService;

	@Autowired
	private FileService fileService;

	@Value("${project.image.posts}")
	private String path;

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
			@RequestParam(value = "pageNumber", defaultValue = ApplicationConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = ApplicationConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = ApplicationConstants.POST_SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = ApplicationConstants.SORT_DIRECTION, required = false) String sortDir) {

		PaginatedResponse<PostDto> postsByCategory = this.postService.getPostsByCategory(categoryId, pageNumber - 1,
				pageSize, sortBy, sortDir);
		return new ResponseEntity<>(postsByCategory, HttpStatus.OK);
	}

	// controller for get posts for user
	@GetMapping("/user/{userId}/posts")
	public ResponseEntity<PaginatedResponse<PostDto>> getAllPostsByUser(@PathVariable Integer userId,
			@RequestParam(value = "pageNumber", defaultValue = ApplicationConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = ApplicationConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = ApplicationConstants.POST_SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = ApplicationConstants.SORT_DIRECTION, required = false) String sortDir) {

		PaginatedResponse<PostDto> postsByUser = this.postService.getAllPostsByUser(userId, pageNumber - 1, pageSize,
				sortBy, sortDir);
		return new ResponseEntity<>(postsByUser, HttpStatus.OK);
	}

	// controller to get all posts
	// URL => localhost:8080/api/posts?pageNUmber=2&pageSize=4
	@GetMapping("/posts")
	public ResponseEntity<PaginatedResponse<PostDto>> getAllPosts(
			@RequestParam(value = "pageNumber", defaultValue = ApplicationConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = ApplicationConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = ApplicationConstants.POST_SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = ApplicationConstants.SORT_DIRECTION, required = false) String sortDir) {
		// for pagination we will get pageNumber and pageSize from url
		// page number always start from zero so whatever comes from url, we pass to
		// function after decrementing by 1

		PaginatedResponse<PostDto> allPosts = this.postService.getAllPosts(pageNumber - 1, pageSize, sortBy, sortDir);
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

	// Method for search posts by keyword
	@GetMapping("posts/search/{keyword}")
	public ResponseEntity<List<PostDto>> searchPostsByKeyword(@PathVariable String keyword) {
		List<PostDto> postDtos = this.postService.getPostsByKeyword(keyword);
		return new ResponseEntity<List<PostDto>>(postDtos, HttpStatus.OK);
	}

	// controller to upload image to databse
	@PostMapping("/posts/image/upload/{postId}")
	public ResponseEntity<FileResponse<PostDto>> postImage(@RequestParam("image") MultipartFile image,
			@PathVariable Integer postId) throws IOException {

		PostDto post = this.postService.getPostById(postId);

		String uploadImage = this.fileService.uploadImage(this.path, image);// here we will handle exception using
																			// Global Exception handler

		post.setImage(uploadImage);
		PostDto updatedPost = this.postService.updatePost(post, postId);

		FileResponse<PostDto> response = new FileResponse<PostDto>(updatedPost, true);

		return new ResponseEntity<FileResponse<PostDto>>(response, HttpStatus.OK);
	}

	// controller to serve images
	@GetMapping(value = "/posts/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE) // on firing this url in the
																							// browser image will get
																							// displayed
	public void DownloadImage(@PathVariable String imageName, HttpServletResponse response) throws IOException {
		InputStream resource = this.fileService.getResource(this.path, imageName);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource, response.getOutputStream());
	}

}
