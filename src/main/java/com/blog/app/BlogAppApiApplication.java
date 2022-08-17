package com.blog.app;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.blog.app.entities.Role;
import com.blog.app.repositories.RoleRepository;
import com.blog.app.utils.ApplicationConstants;

@SpringBootApplication
public class BlogAppApiApplication implements CommandLineRunner {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(BlogAppApiApplication.class, args);
	}

	// model mapper is used to map properties of one object to another
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println(this.passwordEncoder.encode("Akash@123"));

		// for the first time when there are no roles then two roles have to be made in
		// database automatically
		Role role1 = new Role(ApplicationConstants.ADMIN_USER, "ROLE_ADMIN");
		Role role2 = new Role(ApplicationConstants.NORMAL_USER, "ROLE_NORMAL");

		List<Role> roles = new ArrayList<Role>();
		roles.add(role2);
		roles.add(role1);

		List<Role> savedAll = this.roleRepository.saveAll(roles);

		savedAll.forEach(r -> {
			System.out.println(r.getRoleName());
		});

	}

}
