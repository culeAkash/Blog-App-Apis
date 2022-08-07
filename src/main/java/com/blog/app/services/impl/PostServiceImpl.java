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
import com.blog.app.utils.SortingAndPaginationUtils;

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

	@Autowired
	private SortingAndPaginationUtils<Post, PostDto> utils;

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

		// Implementing sorting here
		Sort sort = this.utils.getSortObject(sortBy, sortDir);

		// Implementing Pagination now

		// Make Pageable object
		Pageable p = PageRequest.of(pageNumber, pageSize, sort);

		// get all posts by page

		Page<Post> pagePost = this.postRepository.findAll(p);

		// Adding all response to Paginated Response Object
		PaginatedResponse<PostDto> pageToPaginatedResponse = this.utils.pageToPaginatedResponse(pagePost);

		List<Post> posts = pagePost.getContent();

		List<PostDto> postDtoObjects = new ArrayList<PostDto>();
		for (Post post : posts) {
			postDtoObjects.add(this.postToPostDto(post));
		}

		pageToPaginatedResponse.setContent(postDtoObjects);
		return pageToPaginatedResponse;
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

		// Implementing sorting here
		Sort sort = this.utils.getSortObject(sortBy, sortDir);

		// implementing pagination
		Pageable p = PageRequest.of(pageNumber, pageSize, sort);
		// get all posts of a category and page attributes
		Page<Post> pagePost = this.postRepository.findByCategory(category, p);

		// Adding all response to Paginated Response Object
		PaginatedResponse<PostDto> pageToPaginatedResposnse = this.utils.pageToPaginatedResponse(pagePost);

		List<Post> posts = pagePost.getContent();

		List<PostDto> postDtoObjects = new ArrayList<PostDto>();
		for (Post post : posts) {
			postDtoObjects.add(this.postToPostDto(post));
		}
		pageToPaginatedResposnse.setContent(postDtoObjects);

		return pageToPaginatedResposnse;
	}

	@Override
	public PaginatedResponse<PostDto> getAllPostsByUser(Integer userId, Integer pageNumber, Integer pageSize,
			String sortBy, String sortDir) {
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "User Id", userId));

		// Implementing sorting here
		Sort sort = this.utils.getSortObject(sortBy, sortDir);

		// implementing pagination
		Pageable p = PageRequest.of(pageNumber, pageSize, sort);

		Page<Post> pagePost = this.postRepository.findByUser(user, p);

		// Adding all response to Paginated Response Object
		PaginatedResponse<PostDto> pageToPaginatedResposnse = this.utils.pageToPaginatedResponse(pagePost);

		List<Post> posts = pagePost.getContent();

		List<PostDto> postDtoObjects = new ArrayList<PostDto>();
		for (Post post : posts) {
			postDtoObjects.add(this.postToPostDto(post));
		}

		pageToPaginatedResposnse.setContent(postDtoObjects);

		return pageToPaginatedResposnse;
	}

	@Override
	public List<PostDto> getPostsByKeyword(String keyword) {
		List<Post> posts = this.postRepository.searchPostsBykeyword("%" + keyword + "%");

		List<PostDto> dtos = new ArrayList<PostDto>();
		for (Post post : posts) {
			dtos.add(this.postToPostDto(post));
		}

		return dtos;
	}

	private Post postDtoToPost(PostDto dtoObject) {
		return this.mapper.map(dtoObject, Post.class);
	}

	private PostDto postToPostDto(Post post) {
		return this.mapper.map(post, PostDto.class);
	}

}
