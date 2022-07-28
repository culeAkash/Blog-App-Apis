package com.blog.app.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.app.entities.User;
import com.blog.app.exceptions.ResourceNotFoundException;
import com.blog.app.payloads.UserDto;
import com.blog.app.repositories.UserRepository;
import com.blog.app.services.UserService;

@Service
public class userServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;// we will save and update user here

	@Override
	public UserDto createUser(UserDto userDtoObject) {
		// here we have UserDto object but userRepo will expect User object so we have
		// to change dto to user
		User user = this.dtoToUser(userDtoObject);
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
	public List<UserDto> getAllusers() {
		List<User> users = this.userRepository.findAll();

		List<UserDto> userDtos = new ArrayList<UserDto>();

		for (User user : users) {
			userDtos.add(this.userToDto(user));
		}

		return userDtos;
	}

	@Override
	public void deleteUser(Integer userId) {
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "User Id", userId));// if user with id is not
																								// in database throw
																								// this exception
		this.userRepository.delete(user);

	}

	// method to change userDto to User
	private User dtoToUser(UserDto userDtoObject) {
		User user = new User();
		user.setId(userDtoObject.getId());
		user.setName(userDtoObject.getName());
		user.setEmail(userDtoObject.getEmail());
		user.setPassword(userDtoObject.getPassword());
		user.setAbout(userDtoObject.getAbout());
		return user;
	}

	// method to change UserDto to User
	private UserDto userToDto(User user) {
		UserDto dto = new UserDto();
		dto.setId(user.getId());
		dto.setName(user.getName());
		dto.setEmail(user.getEmail());
		dto.setPassword(user.getPassword());
		dto.setAbout(user.getAbout());

		return dto;
	}

}
