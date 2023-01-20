package com.blog.app.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.app.payloads.ApiResponse;
import com.blog.app.payloads.CommentDto;
import com.blog.app.services.CommentService;

@RestController
@RequestMapping("/api/v1")
public class CommentController {

	@Autowired
	private CommentService commentSerice;

	@PostMapping("/user/{userId}/post/{postId}/comments")
	public ResponseEntity<CommentDto> createComment(@Valid @RequestBody CommentDto commentDto,
			@PathVariable Integer postId, @PathVariable Integer userId) {

		CommentDto createdComment = this.commentSerice.createComment(commentDto, postId, userId);

		return new ResponseEntity<CommentDto>(createdComment, HttpStatus.CREATED);

	}

	@DeleteMapping("/comment/{commentId}")
	public ResponseEntity<ApiResponse> deleteComment(@PathVariable Integer commentId) {

		this.commentSerice.deleteComment(commentId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Comment has been deleted successfully", true),
				HttpStatus.OK);
	}

	@PutMapping("/comment/{commentId}")
	public ResponseEntity<CommentDto> updateComment(@Valid @RequestBody CommentDto commentDto,
			@PathVariable Integer commentId) {

		CommentDto updatedComment = this.commentSerice.updateComment(commentDto, commentId);

		return new ResponseEntity<CommentDto>(updatedComment, HttpStatus.CREATED);

	}

}
