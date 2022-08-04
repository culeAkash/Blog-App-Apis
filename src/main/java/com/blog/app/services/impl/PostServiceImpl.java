package com.blog.app.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.blog.app.entities.Category;
import com.blog.app.entities.Post;
import com.blog.app.entities.User;
import com.blog.app.exceptions.ResourceNotFoundException;
import com.blog.app.payloads.PaginatedResponse;
import com.blog.app.payloads.PostDto;
import com.blog.app.repositories.CategoryRepository;
import com.blog.app.repositories.PostRepository;
import com.blog.app.repositories.UserRepository;
import com.blog.app.services.PostService;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private ModelMapper mapper;// to map Dto to Entity and vice versa

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public PostDto createPost(PostDto postDto, Integer userId, Integer categoryId) {
		Post post = this.postDtoToPost(postDto);
		// set image
		post.setPostImage("default.png");
		// set date
		post.setAddedDate(new Date());

		// set user
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "User Id", userId));
		post.setUser(user);

		// set category
		Category category = this.categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryId));
		post.setCategory(category);

		this.postRepository.save(post);
		return this.postToPostDto(post);
	}

	@Override
	public PostDto updatePost(PostDto postDto, Integer postId) {
		// get post by id from the repo
		Post post = this.postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "Post Id", postId));

		// update old post details
		post.setPostTitle(postDto.getPostTitle());
		post.setPostContent(postDto.getPostContent());

		// save new updated post
		this.postRepository.save(post);
		return this.postToPostDto(post);
	}

	@Override
	public void deletePost(Integer postId) {
		Post post = this.postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "Post Id", postId));
		this.postRepository.delete(post);
	}

	@Override
	public PaginatedResponse<PostDto> getAllPosts(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
		// Implementing Pagination now

		// if asc then in ascending order else in desc
		Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy) : Sort.by(sortBy).descending();

		// Make Pageable object
		Pageable p = PageRequest.of(pageNumber, pageSize, sort);// Sort.by returns a Sort object

		// Implementing sorting here

		// get all posts by page

		Page<Post> pagePost = this.postRepository.findAll(p);

		// send proper response when pagination is implemented
		PaginatedResponse<PostDto> response = new PaginatedResponse<PostDto>();
		response.setPageNumber(pagePost.getNumber() + 1);
		response.setIsLastPage(pagePost.isLast());
		response.setPageSize(pagePost.getSize());
		response.setTotalElements(pagePost.getTotalElements());
		response.setTotalPages(pagePost.getTotalPages());

		List<Post> posts = pagePost.getContent();

		List<PostDto> postDtoObjects = new ArrayList<PostDto>();
		for (Post post : posts) {
			postDtoObjects.add(this.postToPostDto(post));
		}

		response.setContent(postDtoObjects);
		return response;
	}

	@Override
	public PostDto getPostById(Integer postId) {
		Post post = this.postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "Post Id", postId));

		PostDto postDto = this.postToPostDto(post);
		return postDto;
	}

	@Override
	public PaginatedResponse<PostDto> getPostsByCategory(Integer categoryId, Integer pageNumber, Integer pageSize,
			String sortBy, String sortDir) {
		Category category = this.categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryId));

		// if asc then in ascending order else in desc
		Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy) : Sort.by(sortBy).descending();

		// Make Pageable object
		Pageable p = PageRequest.of(pageNumber, pageSize, sort);// Sort.by returns a Sort object

		// get all posts of a category and page attributes
		Page<Post> pagePost = this.postRepository.findByCategory(category, p);

		// send proper response when pagination is implemented
		PaginatedResponse<PostDto> response = new PaginatedResponse<PostDto>();
		response.setPageNumber(pagePost.getNumber() + 1);
		response.setIsLastPage(pagePost.isLast());
		response.setPageSize(pagePost.getSize());
		response.setTotalElements(pagePost.getTotalElements());
		response.setTotalPages(pagePost.getTotalPages());

		List<Post> posts = pagePost.getContent();

		List<PostDto> postDtoObjects = new ArrayList<PostDto>();
		for (Post post : posts) {
			postDtoObjects.add(this.postToPostDto(post));
		}

		response.setContent(postDtoObjects);

		return response;
	}

	@Override
	public PaginatedResponse<PostDto> getAllPostsByUser(Integer userId, Integer pageNumber, Integer pageSize,
			String sortBy, String sortDir) {
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "User Id", userId));

		// if asc then in ascending order else in desc
		Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy) : Sort.by(sortBy).descending();

		// Make Pageable object
		Pageable p = PageRequest.of(pageNumber, pageSize, sort);// Sort.by returns a Sort object

		Page<Post> pagePost = this.postRepository.findByUser(user, p);

		// send proper response when pagination is implemented
		PaginatedResponse<PostDto> response = new PaginatedResponse<PostDto>();
		response.setPageNumber(pagePost.getNumber() + 1);
		response.setIsLastPage(pagePost.isLast());
		response.setPageSize(pagePost.getSize());
		response.setTotalElements(pagePost.getTotalElements());
		response.setTotalPages(pagePost.getTotalPages());

		List<Post> posts = pagePost.getContent();

		List<PostDto> postDtoObjects = new ArrayList<PostDto>();
		for (Post post : posts) {
			postDtoObjects.add(this.postToPostDto(post));
		}

		response.setContent(postDtoObjects);

		return response;
	}

	@Override
	public List<PostDto> getPostsByKeyword(String keyword) {
		// TODO Auto-generated method stub
		return null;
	}

	private Post postDtoToPost(PostDto dtoObject) {
		return this.mapper.map(dtoObject, Post.class);
	}

	private PostDto postToPostDto(Post post) {
		return this.mapper.map(post, PostDto.class);
	}

}
