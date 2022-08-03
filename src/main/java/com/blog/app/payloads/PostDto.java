package com.blog.app.payloads;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {

	private Integer postId;

	@NotEmpty
	@Size(min = 5, max = 50, message = "Post Title must be of size between 5 and 50")
	private String postTitle;

	@NotEmpty
	@Size(min = 5, max = 50, message = "Post Title must be of size between 5 and 50")
	private String postContent;
	private String image;
	private Date addedDate;

	// As User class has Post object in use infinite recursion happens hence give
	// exception
	private UserDto user;

	// Same infinite recursion also occurs in case of Category class
	private CategoryDto category;
}
