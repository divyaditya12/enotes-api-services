package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Category;

public interface CategoryService {

    public Boolean saveCategory(Category category);

    public List<Category> getAllCategory();

}
