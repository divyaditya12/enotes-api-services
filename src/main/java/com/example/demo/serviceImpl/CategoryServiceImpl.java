package com.example.demo.serviceImpl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.w3c.dom.CDATASection;

import com.example.demo.categoryDto.CategoryDto;
import com.example.demo.categoryDto.CategoryResponseDto;
import com.example.demo.entity.Category;
import com.example.demo.exception.ResourceNotFound;
import com.example.demo.exception.ValidationException;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.CategoryService;
import com.example.demo.util.Validation;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private Validation validation;

    @Override
    public Boolean saveCategory(CategoryDto categoryDto) {
        // Category category = new Category();
        // category.setName(categoryDto.getName());
        // category.setDescription(categoryDto.getDescription());
        // category.setIsActive(categoryDto.getIsActive());

        // Checking Validation
        validation.categoryValidation(categoryDto);

        Category category = modelMapper.map(categoryDto, Category.class);
        if (ObjectUtils.isEmpty(category.getId())) {
            category.setIsDeleted(false);
            // category.setCreatedBy(1);
            // category.setCreatedOn(new Date());
        } else {
            updateCategory(category);
        }

        Category saveCategory = categoryRepository.save(category);
        if (ObjectUtils.isEmpty(saveCategory)) {
            return false;
        }
        return true;
    }

    public void updateCategory(Category category) {
        Optional<Category> id = categoryRepository.findById(category.getId());
        Category existingCategory = id.get();
        category.setCreatedBy(existingCategory.getCreatedBy());
        category.setCreatedOn(existingCategory.getCreatedOn());
        category.setIsDeleted(existingCategory.getIsDeleted());

        // category.setUpdatedBy(1);
        // category.setUpdatedOn(new Date());

    }

    @Override
    public List<CategoryDto> getAllCategory() {
        List<Category> categories = categoryRepository.findByIsDeletedFalse();
        List<CategoryDto> categoryDtoList = categories.stream().map(cat -> modelMapper.map(cat, CategoryDto.class))
                .toList();
        return categoryDtoList;
    }

    @Override
    public List<CategoryResponseDto> getActiveCategories() {
        List<Category> categories = categoryRepository.findByIsActiveTrueAndIsDeletedFalse();
        List<CategoryResponseDto> categoryResponseDtoList = categories.stream()
                .map(cat -> modelMapper.map(cat, CategoryResponseDto.class)).toList();
        return categoryResponseDtoList;
    }

    public CategoryDto getCategoryById(Integer id) throws Exception {
        Category category = categoryRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFound("category not found with id=" + id));
        if (!ObjectUtils.isEmpty(category)) {
            return modelMapper.map(category, CategoryDto.class);
        }
        return null;
    }

    @Override
    public Boolean deleteCategoryById(Integer id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            Category category2 = category.get();
            category2.setIsDeleted(true);
            categoryRepository.save(category2);
            return true;
        }
        return false;
    }

}
