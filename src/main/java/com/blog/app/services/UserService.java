package com.blog.app.services;

import java.util.List;

import com.blog.app.payloads.UserDto;

public interface UserService {
	UserDto createUser(UserDto user);

	UserDto updateUser(UserDto user, Integer id);

	UserDto getUserById(Integer userId);

	List<UserDto> getAllusers(Integer pageNumber, Integer pageSize);

	void deleteUser(Integer userId);
}
