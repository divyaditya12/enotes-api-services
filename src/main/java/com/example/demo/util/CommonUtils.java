package com.example.demo.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.demo.handler.GenericResponse;

import jakarta.servlet.http.HttpServletRequest;

public class CommonUtils {

    public static ResponseEntity<?> createBuildResponse(Object data, HttpStatus status) {
        GenericResponse response = GenericResponse.builder()
                .responseStatus(status)
                .status("success")
                .message("success")
                .data(data)
                .build();
        return response.create();
    }

    public static ResponseEntity<?> createBuildResponseMessage(String message, HttpStatus status) {
        GenericResponse response = GenericResponse.builder()
                .responseStatus(status)
                .status("success")
                .message(message)
                .build();
        return response.create();
    }

    public static ResponseEntity<?> createErrorResponse(Object data, HttpStatus status) {
        GenericResponse response = GenericResponse.builder()
                .responseStatus(status)
                .status("failure")
                .message("failure")
                .data(data)
                .build();
        return response.create();
    }

    public static ResponseEntity<?> createErrorResponseMessage(String message, HttpStatus status) {
        GenericResponse response = GenericResponse.builder()
                .responseStatus(status)
                .status("failure")
                .message(message)
                .build();
        return response.create();
    }

    public static String getContentType(String originalFileName) {
        String extension = FilenameUtils.getExtension(originalFileName);
        switch (extension) {
            case "pdf":
                return "application/pdf";
            case "xlsx":
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "txt":
                return "text/plain";
            case "png":
                return "image/png";
            case "jpeg":
            case "jpg":
                return "image/jpeg";
            default:
                return "application/octet-stream";
        }
    }

    public static String getUrl(HttpServletRequest request) {
        String apiUrl = request.getRequestURL().toString();
        apiUrl = apiUrl.replace(request.getServletPath(), "");
        return apiUrl;
    }

}
