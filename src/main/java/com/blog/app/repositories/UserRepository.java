package com.blog.app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blog.app.entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	@Query("select u from User u where  u.name like :key OR u.email like :key OR u.about like :key OR u.userId like :key")
	List<User> searchUsersByKeyword(@Param("key") String key);
}
