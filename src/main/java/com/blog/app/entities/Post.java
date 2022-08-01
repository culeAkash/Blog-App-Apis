package com.blog.app.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "posts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer postId;
	private String postTitle;
	private String postContent;
	private String postImage;
	private Date addedDate;

	// Category to which this post belongs
	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	// Which user posted this
	@ManyToOne
	private User user;

}
