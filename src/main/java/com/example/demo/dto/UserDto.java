package com.example.demo.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String mobNo;

    private List<RoleDto> roles;

    @Data
    public static class RoleDto {
        private Integer id;
        private String name;
    }
}
