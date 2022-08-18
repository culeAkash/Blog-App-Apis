package com.blog.app.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

	public static final String AUTH_HEADER = "Authorization";

	@Bean
	public Docket api() {

		return new Docket(DocumentationType.SWAGGER_2).apiInfo(this.getInfo()).securityContexts(this.securityContexts())
				.securitySchemes(Arrays.asList(this.apiKeys())).select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any()).build();
	}

	private ApiInfo getInfo() {
		return new ApiInfo("Blogging Application Api", "This project is developed by Akash Jaiswal", "1.0",
				"Terms of Service", new Contact("Akash Jaiswal", null, "akashjaiswal3120@gmail.com"), "License of APis",
				"Api licecse", Collections.emptyList());
	}

	private ApiKey apiKeys() {
		return new ApiKey("JWT", AUTH_HEADER, "header");
	}

	private List<SecurityContext> securityContexts() {
		return Arrays.asList(SecurityContext.builder().securityReferences(this.securityReferences()).build());
	}

	private List<SecurityReference> securityReferences() {

		AuthorizationScope scope = new AuthorizationScope("global", "accessEvertything");

		return Arrays.asList(new SecurityReference("JWT", new AuthorizationScope[] { scope }));
	}
}
