package com.springboot.blog;

import com.springboot.blog.entity.Role;
import com.springboot.blog.repository.RoleRepository;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Spring Boot Blog App REST APIs",
				description = "Spring Boot App REST APIs Documentation",
				version = "v1.0",
				contact = @Contact(
						name = "Manhong",
						email = "yuan.manh@northeastern.edu",
						url = "https://www.linkedin.com/in/manhong-yuan/"
				)
				/*license = @License(
						name = "Apache 2.0",
						url = ".../license"
				)*/
		),
		externalDocs = @ExternalDocumentation(
				description = "Spring Boot Blog App Documentation",
				url = "https://github.com/YuanManhong/BlogApp"
		)
)
public class SpringbootBlogRestApiApplication implements CommandLineRunner {
	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}
	public static void main(String[] args) {
		SpringApplication.run(SpringbootBlogRestApiApplication.class, args);
	}

	@Autowired
	private RoleRepository roleRepository;
	@Override
	public void run(String... args) throws Exception {
		Role adminRole = new Role();
		adminRole.setName("ROLE_ADMIN");
		roleRepository.save(adminRole);

		Role userRole = new Role();
		userRole.setName("ROLE_USER");
		roleRepository.save(userRole);
	}
}

/*
In this code, ModelMapper is set up to be shared across the entire program. This is done using Spring Boot's features, which is a framework used to build Java applications easily.

Sharing Tool: By tagging ModelMapper with @Bean, Spring Boot is told to create a single ModelMapper tool that can be shared wherever needed in the program. This way, you don't have to create a new ModelMapper every time you need it, which saves resources.

Organized Setup: Having ModelMapper set up in this central class makes it easy to manage. If you want to change how ModelMapper works, you only need to do it in one place.

Easy Testing: When you test parts of your program, Spring Boot makes it easy to replace this ModelMapper with a simplified version, which can make testing easier.

Consistent Pattern: This setup follows common practices in Spring Boot, making the code familiar to other developers who work on it.
 */


/*
	To insert matadata in table:
		1. add data.sql and schema.sql in resources folder
		2. let SpringbootBlogApiApplication implement CommandLineRunner and save default data in repository
		3. manually insert records by executing the insertion statement
 */

/*
	Autowired:

	1.	When you use @Autowired, it's like asking Spring Boot to magically put the right tools into your bag for you.
		You just tell Spring Boot what type of tools you need (for example, a RoleRepository),
		and Spring Boot finds those tools and puts them in your bag automatically.

	2.  if a class has only one constructor and that constructor has no annotations like @Autowired,
		Spring will automatically consider it as an implicit constructor injection point.
		This means that Spring will automatically inject the dependencies into the constructor without
		you having to explicitly use @Autowired.
 */