package com.blog.app.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtTokenHelper tokenHelper;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

//		1.Get Token
		String requestToken = request.getHeader("Authorization");// get authorization header from request
		// Right now the token will be in format Bearer haiojfeipfj7809, so we have to
		// remove the Bearer

		String username = null;
		String token = null;

		if (requestToken != null && requestToken.startsWith("Bearer")) {

			token = requestToken.substring(7);

			try {
				username = this.tokenHelper.getUsernameFromToken(token);// many exceptions can be generated here
			} catch (IllegalArgumentException e) {
				System.out.println("Unable to get Jwt Token");
			} catch (ExpiredJwtException e) {
				System.out.println("Jwt Token has expired");
			} catch (MalformedJwtException e) {
				System.out.println("Invalid Jwt Token");
			}
		} else {
			System.out.println("Token does not contain Bearer");
		}

		// Once we get Token now we have to validate

		if (username != null && SecurityContextHolder.getContext()
				.getAuthentication() == null/* right now security is authenticating no one */) {

			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

			if (this.tokenHelper.validateToken(token, userDetails)) {
				// sahi chal raha h
				// authentication karna h

				UsernamePasswordAuthenticationToken usernamePasswordAuthToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());

				usernamePasswordAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthToken);
			} else {
				System.out.println("Invalid Jwt Token");
			}
		} else {
			System.out.println("username is null or context is not null");
		}

		filterChain.doFilter(request, response);// it will check before all api requests if our auth is valid or not

	}

}
