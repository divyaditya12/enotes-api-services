package com.example.demo.dto;

import java.util.Date;

import com.example.demo.entity.FileDetails;

import lombok.Data;

@Data
public class NotesDto {

    private Integer id;
    private String title;
    private String description;
    private CategoryDto category;

    private Integer createdBy;
    private Date createdOn;
    private Integer updatedBy;
    private Date updatedOn;
    private FileDto fileDetails;

    @Data
    public static class FileDto {
        private Integer id;

        private String originalFileName;
        private String displayFileName;

    }

    @Data
    public static class CategoryDto {
        private Integer id;
        private String name;
    }
}
