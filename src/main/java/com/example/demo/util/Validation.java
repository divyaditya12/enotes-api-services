package com.example.demo.util;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.example.demo.dto.CategoryDto;
import com.example.demo.exception.ValidationException;

@Component
public class Validation {

    public void categoryValidation(CategoryDto categoryDto) {
        Map<String, Object> error = new LinkedHashMap<>();

        if (ObjectUtils.isEmpty(categoryDto)) {
            throw new IllegalArgumentException("Category object should not be null");
        } else {
            // name validation
            if (ObjectUtils.isEmpty(categoryDto.getName())) {
                error.put("name", "Name field is empty or null");
            } else {
                if (categoryDto.getName().length() < 3) {
                    error.put("name", "name length min 3");
                }
                if (categoryDto.getName().length() > 100) {
                    error.put("name", "name length max 100");
                }
            }
            // description validation
            if (ObjectUtils.isEmpty(categoryDto.getDescription())) {
                error.put("description", "description field in empty or null");
            }
            // isActive validation
            if (ObjectUtils.isEmpty(categoryDto.getIsActive())) {
                error.put("isActive", "isActive field is empty or null");
            } else {
                if (categoryDto.getIsActive() != Boolean.TRUE
                        && categoryDto.getIsActive() != Boolean.FALSE) {
                    error.put("isActive", "isActive field is invalid ");
                }
            }
        }
        if (!error.isEmpty()) {

            throw new ValidationException(error);
        }
    }
}
