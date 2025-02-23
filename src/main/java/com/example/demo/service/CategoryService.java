package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.CategoryDto;
import com.example.demo.dto.CategoryResponseDto;
import com.example.demo.entity.Category;
import com.example.demo.exception.ResourceNotFound;

public interface CategoryService {

    public Boolean saveCategory(CategoryDto categoryDto);

    public List<CategoryDto> getAllCategory();

    public List<CategoryResponseDto> getActiveCategories();

    public CategoryDto getCategoryById(Integer id) throws Exception;

    public Boolean deleteCategoryById(Integer id);
}
