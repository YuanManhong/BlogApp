package com.springboot.blog.service;

import com.springboot.blog.payload.CategoryDTO;

public interface CategoryService {
    CategoryDTO addCategory(CategoryDTO categoryDTO);
    CategoryDTO getCategory(Long categoryId);

}
