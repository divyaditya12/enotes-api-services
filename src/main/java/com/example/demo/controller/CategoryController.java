package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Category;
import com.example.demo.service.CategoryService;

@RestController
@RequestMapping("api/v1/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/save-category")
    public ResponseEntity<?> saveCategory(@RequestBody Category category) {
        Boolean saveCategory = categoryService.saveCategory(category);
        if (saveCategory) {
            return new ResponseEntity<>("saved successfully", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("not saved", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-categories")
    public ResponseEntity<?> getAllCategories() {
        List<Category> categories = categoryService.getAllCategory();
        if (CollectionUtils.isEmpty(categories)) {
            return ResponseEntity.noContent().build();
        } else {
            return new ResponseEntity<>(categories, HttpStatus.OK);
        }
    }

}
