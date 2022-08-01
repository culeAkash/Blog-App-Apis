package com.blog.app.payloads;

import java.util.Date;

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
	private String postTitle;
	private String postContent;
	private String image;
	private Date addedDate;

	// As User class has Post object in use infinite recursion happens hence give
	// exception
	private UserDto user;

	// Same infinite recursion also occurs in case of Category class
	private CategoryDto category;
}
