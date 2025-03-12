package com.example.demo.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.example.demo.dto.CategoryDto;
import com.example.demo.dto.UserDto;
import com.example.demo.exception.ExistDataException;
import com.example.demo.exception.ValidationException;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;

@Component
public class Validation {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

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

    public void userValidation(UserDto userDto) {
        if (!StringUtils.hasText(userDto.getFirstName())) {
            throw new IllegalArgumentException("Invalid first name");
        }

        if (!StringUtils.hasText(userDto.getLastName())) {
            throw new IllegalArgumentException("Invalid last name");
        }

        if (!StringUtils.hasText(userDto.getEmail()) || !userDto.getEmail().matches(Constants.EMAIL_REGEX)) {
            throw new IllegalArgumentException("Invalid email id");
        } else {
            Boolean isExist = userRepository.existsByEmail(userDto.getEmail());
            if (isExist) {
                throw new ExistDataException("email already exist");
            }
        }

        if (!StringUtils.hasText(userDto.getMobNo()) || !userDto.getMobNo().matches(Constants.MOBILE_REGEX)) {
            throw new IllegalArgumentException("Invalid mobile number");
        }

        if (CollectionUtils.isEmpty(userDto.getRoles())) {
            throw new IllegalArgumentException("invalid role");
        } else {
            List<Integer> roleIds = roleRepository.findAll().stream().map(r -> r.getId()).toList();

            List<Integer> invalidIds = userDto.getRoles().stream().map(r -> r.getId())
                    .filter(roleId -> !roleIds.contains(roleId)).toList();

            if (!CollectionUtils.isEmpty(invalidIds)) {
                throw new IllegalArgumentException("role is invalid" + invalidIds);
            }
        }

    }
}
