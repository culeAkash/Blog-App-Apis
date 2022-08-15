package com.blog.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.app.exceptions.InvalidCredentialsException;
import com.blog.app.payloads.JwtAuthResponse;
import com.blog.app.security.JwtAuthRequest;
import com.blog.app.security.JwtTokenHelper;

@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenHelper tokenHelper;

	@PostMapping("/login")
	public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest request)
			throws InvalidCredentialsException {

		this.authenticate(request.getUsername(), request.getPassword());

		// if authenticated now generate token

		UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getUsername());

		String token = this.tokenHelper.generateToken(userDetails);

		JwtAuthResponse response = new JwtAuthResponse();
		response.setToken(token);

		return new ResponseEntity<JwtAuthResponse>(response, HttpStatus.OK);
	}

	private void authenticate(String username, String password) throws InvalidCredentialsException {

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
				password);
		try {
			this.authenticationManager.authenticate(authenticationToken);// if not authenticated throw exception
		} catch (BadCredentialsException e) {
			throw new InvalidCredentialsException("Invalid Username or Password !!!");
		}
	}

}
