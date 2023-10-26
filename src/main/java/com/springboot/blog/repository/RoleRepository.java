package com.springboot.blog.repository;

import com.springboot.blog.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}

/*
Reason of using Optional as a return type
This helps you avoid common problems like getting an error when you try to use something that doesn't exist. Instead, you can check if the box is empty (using methods like .isPresent()) and then do something special if it's empty or open the box to get the role if it's there.

In simpler terms, Optional makes it clear when something might be missing, and it helps you handle that situation without causing errors.
 */