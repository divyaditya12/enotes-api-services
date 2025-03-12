package com.example.demo.exception;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.demo.util.CommonUtils;

@ControllerAdvice
public class GlobalExceptionHandler {

    // @ExceptionHandler(Exception.class)
    // public ResponseEntity<?> handleException(Exception e) {
    // return new ResponseEntity<>(e.getMessage(),
    // HttpStatus.INTERNAL_SERVER_ERROR);
    // }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> handleNullPointerException(Exception e) {
        return CommonUtils.createErrorResponseMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<?> handleResourceNotFoundException(Exception e) {

        return CommonUtils.createErrorResponseMessage(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        Map<String, Object> error = new LinkedHashMap<>();
        allErrors.stream().forEach(er -> {
            String msg = er.getDefaultMessage();
            String field = ((FieldError) (er)).getField();
            error.put(field, msg);
        });
        return CommonUtils.createErrorResponseMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidationException(ValidationException e) {
        return CommonUtils.createErrorResponse(e.getErrors(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExistDataException.class)
    public ResponseEntity<?> handleExistDataException(ExistDataException e) {
        return CommonUtils.createErrorResponseMessage(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {

        return CommonUtils.createErrorResponseMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        return CommonUtils.createErrorResponseMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SuccessException.class)
    public ResponseEntity<?> handleSuccessException(SuccessException e) {
        return CommonUtils.createBuildResponseMessage(e.getMessage(), HttpStatus.OK);

    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException e) {
        return CommonUtils.createErrorResponseMessage(e.getMessage(), HttpStatus.BAD_REQUEST);

    }

}
