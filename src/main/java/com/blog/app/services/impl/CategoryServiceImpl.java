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
	public PaginatedResponse<CategoryDto> getAllCategories(Integer pageNumber, Integer pageSize, String sortBy,
			String sortDir) {

		// implemnting sorting here
		Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy) : Sort.by(sortBy).descending();

		// implementing pagination
		Pageable p = PageRequest.of(pageNumber, pageSize, sort);

		Page<Category> pageCat = this.catRepo.findAll(p);

		// Now we will send modified response wherever pagination is implemented
		PaginatedResponse<CategoryDto> paginatedResponse = new PaginatedResponse<CategoryDto>();
		paginatedResponse.setPageNumber(pageCat.getNumber() + 1);
		paginatedResponse.setPageSize(pageCat.getSize());
		paginatedResponse.setTotalElements(pageCat.getTotalElements());
		paginatedResponse.setTotalPages(pageCat.getTotalPages());
		paginatedResponse.setIsLastPage(pageCat.isLast());

		List<Category> categories = pageCat.getContent();

		List<CategoryDto> dtos = new ArrayList<CategoryDto>();
		categories.forEach((cat) -> {
			dtos.add(this.CategoryToDto(cat));
		});

		paginatedResponse.setContent(dtos);
		return paginatedResponse;
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
