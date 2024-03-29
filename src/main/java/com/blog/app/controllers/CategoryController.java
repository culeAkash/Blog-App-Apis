package com.blog.app.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blog.app.payloads.ApiResponse;
import com.blog.app.payloads.CategoryDto;
import com.blog.app.payloads.PaginatedResponse;
import com.blog.app.services.CategoryService;
import com.blog.app.utils.ApplicationConstants;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {

	@Autowired
	CategoryService categoryService;

	// create
	@PostMapping("/categories")
	public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
		CategoryDto createdCategory = this.categoryService.createCategory(categoryDto);
		return new ResponseEntity<CategoryDto>(createdCategory, HttpStatus.CREATED);
	}

	// update
	@PutMapping("/categories/{categoryId}")
	public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto,
			@PathVariable Integer categoryId) {
		CategoryDto updatedCategory = this.categoryService.updateCategory(categoryDto, categoryId);
		return new ResponseEntity<CategoryDto>(updatedCategory, HttpStatus.CREATED);
	}

	// delete
	@DeleteMapping("/categories/{categoryId}")
	public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Integer categoryId) {
		this.categoryService.deletecategory(categoryId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Deleted Category Successfully", true), HttpStatus.OK);
	}

	// get
	@GetMapping("/categories/{categoryId}")
	public ResponseEntity<CategoryDto> getcategoryById(@PathVariable Integer categoryId) {
		CategoryDto categoryDto = this.categoryService.getCategoryById(categoryId);
		return new ResponseEntity<CategoryDto>(categoryDto, HttpStatus.OK);
	}

	// get all
	@GetMapping("/categories")
	public ResponseEntity<PaginatedResponse<CategoryDto>> getAllcategories(
			@RequestParam(value = "pageNumber", defaultValue = ApplicationConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = ApplicationConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = ApplicationConstants.CATEGORY_SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = ApplicationConstants.SORT_DIRECTION, required = false) String sortDir) {
		PaginatedResponse<CategoryDto> allCategories = this.categoryService.getAllCategories(pageNumber - 1, pageSize,
				sortBy, sortDir);
		return new ResponseEntity<PaginatedResponse<CategoryDto>>(allCategories, HttpStatus.OK);
	}

	// search category controller
	@GetMapping("/categories/search/{keyword}")
	public ResponseEntity<List<CategoryDto>> getCategoriesByKeyword(@PathVariable String keyword) {
		List<CategoryDto> searchedCategoryByKeyword = this.categoryService.searchCategoryByKeyword(keyword);
		return new ResponseEntity<List<CategoryDto>>(searchedCategoryByKeyword, HttpStatus.OK);
	}

}
