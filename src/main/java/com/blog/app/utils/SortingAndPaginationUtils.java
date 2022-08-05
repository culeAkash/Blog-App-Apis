package com.blog.app.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.blog.app.payloads.PaginatedResponse;

@Component
public class SortingAndPaginationUtils<T, E> {

	// To create a paginated response out of a page
	public PaginatedResponse<E> pageToPaginatedResponse(Page<T> pagePost) {
		PaginatedResponse<E> response = new PaginatedResponse<E>();
		response.setPageNumber(pagePost.getNumber() + 1);
		response.setPageSize(pagePost.getSize());
		response.setTotalElements(pagePost.getTotalElements());
		response.setTotalPages(pagePost.getTotalPages());
		response.setLastPage(pagePost.isLast());
		return response;
	}

	// To get sort object from sortBy and SortDirection
	public Sort getSortObject(String sortBy, String sortDir) {
		Sort sort = null;
		if (sortDir.equalsIgnoreCase("asc")) {
			sort = Sort.by(sortBy).ascending();
		} else {
			sort = Sort.by(sortBy).descending();
		}
		return sort;
	}
}
