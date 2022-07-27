package com.blog.app.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.blog.app.entities.User;
import com.blog.app.payloads.UserDto;
import com.blog.app.repositories.UserRepository;
import com.blog.app.services.UserService;

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
	public UserDto updateUser(UserDto user, Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserDto getUserById(Integer userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserDto> getAllusers() {
		// TODO Auto-generated method stub
		return null;
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
