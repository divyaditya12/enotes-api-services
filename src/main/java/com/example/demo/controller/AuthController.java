package com.example.demo.controller;

import java.lang.module.ResolutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.UserDto;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.util.CommonUtils;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto, HttpServletRequest request) throws Exception {
        String url = CommonUtils.getUrl(request);
        Boolean register = userService.registration(userDto, url);
        if (register) {
            return CommonUtils.createBuildResponseMessage("Registration successful", HttpStatus.CREATED);
        } else {
            return CommonUtils.createErrorResponseMessage("Registration failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = userService.login(loginRequest);
        if (ObjectUtils.isEmpty(loginRequest)) {
            return CommonUtils.createErrorResponseMessage("Invalid credential", HttpStatus.BAD_REQUEST);
        }
        return CommonUtils.createBuildResponse(loginResponse, HttpStatus.OK);
    }

}
