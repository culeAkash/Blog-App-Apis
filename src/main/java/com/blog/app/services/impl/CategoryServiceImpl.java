package com.blog.app.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.app.entities.Category;
import com.blog.app.exceptions.ResourceNotFoundException;
import com.blog.app.payloads.CategoryDto;
import com.blog.app.repositories.CategoryRepository;
import com.blog.app.services.CategoryService;

//implemantation for performing all repository related functionalities

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	CategoryRepository catRepo;

	@Autowired
	private ModelMapper mapper;

	@Override
	public CategoryDto createCategory(CategoryDto dto) {
		// we have to add category to database and not dto so we have to change
		Category category = this.CatDtoToCategory(dto);
		this.catRepo.save(category);
		return this.CategoryToDto(category);
	}

	@Override
	public void deletecategory(Integer categoryId) {
		Category category = this.catRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryId));
		this.catRepo.delete(category);
	}

	@Override
	public CategoryDto updateCategory(CategoryDto dto, Integer categoryId) {
		// get old object from repo
		Category category = this.catRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryId));

		// update the properties
		category.setCategoryTitle(dto.getCategoryTitle());
		category.setCategoryDescription(dto.getCategoryDescription());

		// save new object to repository
		this.catRepo.save(category);

		return this.CategoryToDto(category);
	}

	@Override
	public List<CategoryDto> getAllCategories() {
		List<Category> categories = this.catRepo.findAll();

		List<CategoryDto> dtos = new ArrayList<CategoryDto>();
		categories.forEach((cat) -> {
			dtos.add(this.CategoryToDto(cat));
		});

		return dtos;
	}

	@Override
	public CategoryDto getCategoryById(Integer categoryId) {
		Category category = this.catRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryId));

		return this.CategoryToDto(category);
	}

	// method to convert categorydto to category
	private Category CatDtoToCategory(CategoryDto dto) {
		Category category = this.mapper.map(dto, Category.class);
		return category;
	}

	private CategoryDto CategoryToDto(Category category) {
		CategoryDto dto = this.mapper.map(category, CategoryDto.class);
		return dto;
	}

}
