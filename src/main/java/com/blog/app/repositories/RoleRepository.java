package com.blog.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.app.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

}
