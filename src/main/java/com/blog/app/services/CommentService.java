package com.blog.app.services;

import com.blog.app.payloads.CommentDto;

public interface CommentService {

	CommentDto createComment(CommentDto commentDto, Integer postId, Integer userId);

	void deleteComment(Integer commentId);

	CommentDto updateComment(CommentDto commentDto, Integer commentId);
}
