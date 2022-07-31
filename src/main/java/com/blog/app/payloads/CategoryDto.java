package com.blog.app.payloads;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

	// adding validation
	private Integer categoryId;

	@NotEmpty(message = "Category Title must not be empty")
	@Size(min = 5, max = 30, message = "Category Title must be minimum of length 5 and maximum of length 30")
	private String categoryTitle;

	@NotEmpty(message = "Category Description must not be empty")
	@Size(min = 10, max = 50, message = "Category Description must be minimum of length 5 and maximum of length 30")
	private String categoryDescription;
}
