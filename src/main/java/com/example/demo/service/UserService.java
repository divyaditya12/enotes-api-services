package com.example.demo.service;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.UserDto;

public interface UserService {

    public Boolean registration(UserDto userDto, String url) throws Exception;

    public LoginResponse login(LoginRequest loginRequest);

}
