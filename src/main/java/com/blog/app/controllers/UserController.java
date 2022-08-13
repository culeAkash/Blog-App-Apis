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
import com.blog.app.payloads.UserDto;
import com.blog.app.services.FileService;
import com.blog.app.services.UserService;
import com.blog.app.utils.ApplicationConstants;

//Make rest api use RestController
@RestController
@RequestMapping("/api")
public class UserController {

	// for implementing crud operations
	@Autowired
	private UserService userService;

	@Autowired
	private FileService fileService;

	@Value("${project.image.users}")
	private String path;

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

	// controller to upload image to databse
	@PostMapping("/users/image/upload/{userId}")
	public ResponseEntity<FileResponse<UserDto>> postImage(@RequestParam("image") MultipartFile image,
			@PathVariable Integer userId) throws IOException {

		UserDto userDto = this.userService.getUserById(userId);

		String uploadImage = this.fileService.uploadImage(this.path, image);// here we will handle exception using
																			// Global Exception handler

		userDto.setUserImage(uploadImage);
		UserDto updatedUser = this.userService.updateUser(userDto, userId);

		FileResponse<UserDto> response = new FileResponse<UserDto>(updatedUser, true);

		return new ResponseEntity<FileResponse<UserDto>>(response, HttpStatus.OK);
	}

	// controller to serve images
	@GetMapping(value = "/users/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE) // on firing this url in the
																							// browser image will get
																							// displayed
	public void DownloadImage(@PathVariable String imageName, HttpServletResponse response) throws IOException {
		InputStream resource = this.fileService.getResource(this.path, imageName);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource, response.getOutputStream());
	}
}
