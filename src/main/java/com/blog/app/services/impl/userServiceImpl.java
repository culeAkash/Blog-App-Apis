package com.blog.app.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.blog.app.entities.Role;
import com.blog.app.entities.User;
import com.blog.app.exceptions.ResourceNotFoundException;
import com.blog.app.payloads.PaginatedResponse;
import com.blog.app.payloads.UserDto;
import com.blog.app.repositories.RoleRepository;
import com.blog.app.repositories.UserRepository;
import com.blog.app.services.UserService;
import com.blog.app.utils.ApplicationConstants;
import com.blog.app.utils.SortingAndPaginationUtils;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;// we will save and update user here

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private SortingAndPaginationUtils<User, UserDto> utils;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public UserDto registerNewuser(UserDto userDto) {
		User user = this.dtoToUser(userDto);

		// encode the password
		user.setPassword(this.passwordEncoder.encode(user.getPassword()));

		// set role
		Role role = this.roleRepository.findById(ApplicationConstants.NORMAL_USER).get();

		user.getRoles().add(role);

		User savedUser = this.userRepository.save(user);

		return this.userToDto(savedUser);
	}

	@Override
	public UserDto createUser(UserDto userDtoObject) {
		// here we have UserDto object but userRepo will expect User object so we have
		// to change dto to user
		User user = this.dtoToUser(userDtoObject);
		user.setUserImage("deafaultUser.png");
		System.out.println(user.getUserId());
		User savedUser = this.userRepository.save(user);// saves/update user to the database
		return this.userToDto(savedUser);
	}

	@Override
	public UserDto updateUser(UserDto userDtoObject, Integer id) {
		// update user in database
		// from controller user data is coming in form of UserDto, get the user from
		// repo and update info
		User user = this.userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User", "User Id", id));// if user with id is not in
																							// database throw this
																							// exception

		user.setName(userDtoObject.getName());
		user.setAbout(userDtoObject.getAbout());
		user.setPassword(userDtoObject.getPassword());
		user.setEmail(userDtoObject.getEmail());
		user.setUserImage(userDtoObject.getUserImage());

		// save to repo
		this.userRepository.save(user);

		return this.userToDto(user);
	}

	@Override
	public UserDto getUserById(Integer userId) {

		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "User Id", userId));// if user with id is not
																								// in database throw
																								// this exception

		UserDto userDto = this.userToDto(user);
		return userDto;
	}

	@Override
	public PaginatedResponse<UserDto> getAllusers(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {

		// Implementing sorting
		Sort sort = this.utils.getSortObject(sortBy, sortDir);

		// implementing pagination
		Pageable p = PageRequest.of(pageNumber, pageSize, sort);

		Page<User> pageUser = this.userRepository.findAll(p);

		// Improved Response will be now sent after pagination
		PaginatedResponse<UserDto> pageToPaginatedResponse = this.utils.pageToPaginatedResponse(pageUser);

		List<User> users = pageUser.getContent();

		List<UserDto> userDtos = new ArrayList<UserDto>();

		for (User user : users) {
			userDtos.add(this.userToDto(user));
		}

		pageToPaginatedResponse.setContent(userDtos);

		return pageToPaginatedResponse;
	}

	@Override
	public void deleteUser(Integer userId) {
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "User Id", userId));// if user with id is not
																								// in database throw
																								// this exception
		this.userRepository.delete(user);

	}

	// Search method
	@Override
	public List<UserDto> searchUsersByKeyword(String keyword) {
		List<User> users = this.userRepository.searchUsersByKeyword("%" + keyword + "%");

		List<UserDto> dtos = new ArrayList<UserDto>();
		for (User user : users) {
			dtos.add(this.userToDto(user));
		}

		return dtos;

	}

	// method to change userDto to User
	private User dtoToUser(UserDto userDtoObject) {
		User user = this.modelMapper.map(userDtoObject, User.class);
		// map user from userDto

//		user.setId(userDtoObject.getId());
//		user.setName(userDtoObject.getName());
//		user.setEmail(userDtoObject.getEmail());
//		user.setPassword(userDtoObject.getPassword());
//		user.setAbout(userDtoObject.getAbout());
		return user;
	}

	// method to change UserDto to User
	private UserDto userToDto(User user) {
//		UserDto dto = new UserDto();
		// Instead of doing conversion manually use model mapper here
		UserDto dto = this.modelMapper.map(user, UserDto.class);
		// this will map all matching values of user object to userDto object

//		dto.setId(user.getId());
//		dto.setName(user.getName());
//		dto.setEmail(user.getEmail());
//		dto.setPassword(user.getPassword());
//		dto.setAbout(user.getAbout());

		return dto;
	}

}
