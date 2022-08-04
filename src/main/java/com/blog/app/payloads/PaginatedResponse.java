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
	private Integer pageNumber;
	private Integer pageSize;
	private Long totalElements;
	private Integer totalPages;

	private Boolean isLastPage;

	private List<T> content;
}
