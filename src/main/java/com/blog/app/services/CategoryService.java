package com.blog.app.services;

import java.util.List;

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
	PaginatedResponse<CategoryDto> getAllCategories(Integer pageNumber, Integer pageSize, String sortBy,
			String sortDir);

	CategoryDto getCategoryById(Integer id);

	// search category
	List<CategoryDto> searchCategoryByKeyword(String keyword);
}
