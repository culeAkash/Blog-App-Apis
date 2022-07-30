package com.blog.app.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.blog.app.payloads.ApiResponse;

//All exceptions thrown by controllers will be handled by this class
@RestControllerAdvice
public class GlobalExceptionHandler {

//	All ResourceNotFoundException exceptions thrown from controllers are handled by this method
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException e) {
		String message = e.getMessage();
		ApiResponse apiResponse = new ApiResponse(message, false);
		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.NOT_FOUND);
	}

	// This method will handle all the validation exceptions from Controllers
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> hanlderMethodArgNotValidException(MethodArgumentNotValidException ex) {
		Map<String, String> response = new HashMap<String, String>();// to store fields and messages

		// binding results has all errors and for each error get field and message and
		// put to map and send as response
		ex.getBindingResult().getAllErrors().forEach(error -> {
			// get field of the error
			String fieldName = ((FieldError) error).getField();
			String message = error.getDefaultMessage();

			response.put(fieldName, message);
		});

		return new ResponseEntity<Map<String, String>>(response, HttpStatus.BAD_REQUEST);
	}
}
