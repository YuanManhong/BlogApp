package com.springboot.blog.repository;

import com.springboot.blog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
note: Why don't need repository annotation:
Automatic Creation: Just by extending JpaRepository, Spring automatically knows this is a repository. It creates the necessary code in the background.

Error Handling: @Repository helps in converting database errors to Spring errors. But Spring Data JPA does this automatically for you, even if you don't use @Repository.

Finding Beans: Spring looks for components (like services, repositories) to manage. @Repository tells Spring, "Hey, I'm here!". But again, with Spring Data JPA, Spring already knows it's there.
 */
public interface PostRepository extends JpaRepository<Post, Long> {

}
