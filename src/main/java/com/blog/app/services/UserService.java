package com.blog.app.services;

import java.util.List;

import com.blog.app.payloads.PaginatedResponse;
import com.blog.app.payloads.UserDto;

public interface UserService {
	UserDto createUser(UserDto user);

	UserDto updateUser(UserDto user, Integer id);

	UserDto getUserById(Integer userId);

	PaginatedResponse<UserDto> getAllusers(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

	void deleteUser(Integer userId);

	// search users
	List<UserDto> searchUsersByKeyword(String keyword);
}
