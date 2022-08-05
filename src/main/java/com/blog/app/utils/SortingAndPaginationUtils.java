package com.blog.app.utils;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.blog.app.payloads.PaginatedResponse;

@Component
public class SortingAndPaginationUtils<T, E> {

	public PaginatedResponse<E> pageToPaginatedResponse(Page<T> pagePost) {
		PaginatedResponse<E> response = new PaginatedResponse<E>();
		response.setPageNumber(pagePost.getNumber() + 1);
		response.setPageSize(pagePost.getSize());
		response.setTotalElements(pagePost.getTotalElements());
		response.setTotalPages(pagePost.getTotalPages());
		response.setLastPage(pagePost.isLast());
		return response;
	}
}
