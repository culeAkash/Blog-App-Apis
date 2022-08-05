package com.blog.app.payloads;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class PaginatedResponse<T> {
	// Response after Pagination must not only contain LList but also must contain
	// number of pages page number page size number of total elements and if the
	// page is last page or not

	private Integer pageNumber;
	private Integer pageSize;
	private Integer totalPages;
	private Long totalElements;
	private boolean isLastPage;

	private List<T> content;

}
