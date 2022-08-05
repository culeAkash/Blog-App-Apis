package com.blog.app.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.blog.app.entities.Category;
import com.blog.app.exceptions.ResourceNotFoundException;
import com.blog.app.payloads.CategoryDto;
import com.blog.app.payloads.PaginatedResponse;
import com.blog.app.repositories.CategoryRepository;
import com.blog.app.services.CategoryService;
import com.blog.app.utils.SortingAndPaginationUtils;

//implemantation for performing all repository related functionalities

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	CategoryRepository catRepo;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private SortingAndPaginationUtils<Category, CategoryDto> utils;

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
	public PaginatedResponse<CategoryDto> getAllCategories(Integer pageNumber, Integer pagesize, String sortBy,
			String sortDir) {
		// implementing sorting
		Sort sort = this.utils.getSortObject(sortBy, sortDir);

		// implementing pagination
		Pageable p = PageRequest.of(pageNumber, pagesize, sort);

		Page<Category> pageCategory = this.catRepo.findAll(p);

		// Send Improved response after pagination
		PaginatedResponse<CategoryDto> pageToPaginatedResponse = this.utils.pageToPaginatedResponse(pageCategory);

		List<Category> categories = pageCategory.getContent();

		List<CategoryDto> dtos = new ArrayList<CategoryDto>();
		categories.forEach((cat) -> {
			dtos.add(this.CategoryToDto(cat));
		});

		pageToPaginatedResponse.setContent(dtos);

		return pageToPaginatedResponse;
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
