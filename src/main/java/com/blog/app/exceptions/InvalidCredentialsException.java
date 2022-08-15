package com.blog.app.exceptions;

import org.springframework.security.authentication.BadCredentialsException;

public class InvalidCredentialsException extends BadCredentialsException {

	String res;

	public InvalidCredentialsException(String res) {
		super(String.format("%s", res));
		this.res = res;
	}

}
