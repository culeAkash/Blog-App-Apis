package com.blog.app.services;

import java.util.List;

import com.blog.app.payloads.CategoryDto;

public interface CategoryService {

	// create
	CategoryDto createCategory(CategoryDto dto);

	// delete
	void deletecategory(Integer categoryId);

	// update
	CategoryDto updateCategory(CategoryDto dto, Integer id);

	// get
	List<CategoryDto> getAllCategories(Integer pageNumber, Integer pageSize);

	CategoryDto getCategoryById(Integer id);
}
