package com.blog.app.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity

//By using lombok create no arg,all arg constructor and getters and setters
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO) // auto incremenet will happen
	private int id;

	private String name;
	private String email;
	private String password;
	private String about;

	// A user can have many posts
	// user is the column to store user id related to a post
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Set<Post> posts = new HashSet<Post>();

}
