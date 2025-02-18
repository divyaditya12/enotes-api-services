package com.example.demo.handler;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenericResponse {

    private HttpStatus responseStatus;
    private String status; // success failure
    private String message;
    private Object data;

    public ResponseEntity<?> create() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("sattus", status);
        map.put("message", message);
        if (!ObjectUtils.isEmpty(data)) {
            map.put("data", data);
        }
        return new ResponseEntity<>(map, responseStatus);
    }
}
