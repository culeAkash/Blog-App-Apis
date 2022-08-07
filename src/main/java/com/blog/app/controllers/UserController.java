package com.blog.app.controllers;

import java.util.List;

import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blog.app.config.ApplicationConstants;
import com.blog.app.payloads.ApiResponse;
import com.blog.app.payloads.PaginatedResponse;
import com.blog.app.payloads.UserDto;
import com.blog.app.services.UserService;

//Make rest api use RestController
@RestController
@RequestMapping("/api")
public class UserController {

	// for implementing crud operations
	@Autowired
	private UserService userService;

	// POST => Create user
	@PostMapping("/users")
	public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
		// When validation is not satisfied then MethodArgNotValid Exception is thrown
		// which has be handled in GlobalExceptionHandler class
		System.out.println(userDto.getUserId());
		UserDto createdUserDto = this.userService.createUser(userDto);

		return new ResponseEntity<UserDto>(createdUserDto, HttpStatus.CREATED);
	}

	// PUT => Update User
	@PutMapping("/users/{userId}")
	public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto, @PathVariable("userId") int id) {
		UserDto updatedUserDto = this.userService.updateUser(userDto, id);

		return new ResponseEntity<UserDto>(updatedUserDto, HttpStatus.OK);
	}

	// GET => get User

	@GetMapping("/users")
	public ResponseEntity<PaginatedResponse<UserDto>> getAllUsers(
			@RequestParam(value = "pageNumber", defaultValue = ApplicationConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = ApplicationConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = ApplicationConstants.USER_SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = ApplicationConstants.SORT_DIRECTION, required = false) String sortDir) {
		PaginatedResponse<UserDto> allusers = this.userService.getAllusers(pageNumber - 1, pageSize, sortBy, sortDir);

		return ResponseEntity.ok(allusers);
	}

	// get single user by id
	@GetMapping("/users/{userId}")
	public ResponseEntity<UserDto> getAllUsers(@PathVariable int userId) {
		UserDto user = this.userService.getUserById(userId);

		return ResponseEntity.ok(user);
	}

	// DELETE => delete user

	@DeleteMapping("/users/{userId}")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable int userId) {
		this.userService.deleteUser(userId);

		return ResponseEntity.ok(new ApiResponse("User deleted successfully", true));
	}

	// Search user controller
	@GetMapping("/users/search/{keyword}")
	public ResponseEntity<List<UserDto>> getUsersByKeyword(@PathVariable String keyword) {
		List<UserDto> searchedUsersByKeyword = this.userService.searchUsersByKeyword(keyword);
		return new ResponseEntity<List<UserDto>>(searchedUsersByKeyword, HttpStatus.OK);
	}
}
