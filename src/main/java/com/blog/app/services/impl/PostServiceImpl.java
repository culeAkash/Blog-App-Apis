package com.blog.app.services.impl;

import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.app.entities.Category;
import com.blog.app.entities.Post;
import com.blog.app.entities.User;
import com.blog.app.exceptions.ResourceNotFoundException;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deletePost(Integer postId) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Post> getAllPosts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Post getPostById(Integer postId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Post> getPostsByCategory(Integer categoryId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Post> getAllPostsByUser(Integer userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Post> getPostsByKeyword(String keyword) {
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
