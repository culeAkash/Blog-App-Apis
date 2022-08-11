package com.blog.app.payloads;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

	private Integer commentId;

	@NotEmpty
	@Size(min = 10, max = 100, message = "Comment must be of length between 10 and 100")
	private String content;
}
