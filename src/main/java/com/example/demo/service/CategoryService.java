package com.example.demo.service;

import java.util.List;

import com.example.demo.categoryDto.CategoryDto;
import com.example.demo.categoryDto.CategoryResponseDto;
import com.example.demo.entity.Category;

public interface CategoryService {

    public Boolean saveCategory(CategoryDto categoryDto);

    public List<CategoryDto> getAllCategory();

    public List<CategoryResponseDto> getActiveCategories();

    public CategoryDto getCategoryById(Integer id);

    public Boolean deleteCategoryById(Integer id);
}
