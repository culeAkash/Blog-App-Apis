package com.blog.app.payloads;

import java.util.HashSet;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {
	private int userId;

	@NotEmpty
	@Size(min = 4, message = "Username must be minimum of 4 characters")
	private String name;

	@NotBlank(message = "Email must not be blank")
	@Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = "Invalid Email!")
	private String email;

	@NotBlank(message = "Password must not be empty")
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{8,}$", message = "Password must contain atleast 1 uppercase, 1 lowercase, 1 special character and 1 digit ")
	@JsonIgnore
	private String password;

	@NotEmpty
	private String about;

	private String userImage;

	private HashSet<CommentDto> comments = new HashSet<CommentDto>();// to fetch all comments of a user
}
/*
 * We will user User entity only as a table in database In order to receive and
 * send any data through api we will use UserDto class
 */
