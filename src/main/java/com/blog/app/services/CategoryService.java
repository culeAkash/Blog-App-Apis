package com.blog.app.services;

import com.blog.app.payloads.CategoryDto;
import com.blog.app.payloads.PaginatedResponse;

public interface CategoryService {

	// create
	CategoryDto createCategory(CategoryDto dto);

	// delete
	void deletecategory(Integer categoryId);

	// update
	CategoryDto updateCategory(CategoryDto dto, Integer id);

	// get
	PaginatedResponse<CategoryDto> getAllCategories(Integer pageNumber, Integer pageSize);

	CategoryDto getCategoryById(Integer id);
}
