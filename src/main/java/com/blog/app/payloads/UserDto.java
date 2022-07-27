package com.blog.app.payloads;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {
	private int id;
	private String name;
	private String email;
	private String password;
	private String about;
}
/*
 * We will user User entity only as a table in database In order to receive and
 * send any data through api we will use UserDto class
 */
