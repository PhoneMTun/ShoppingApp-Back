package com.bfs.hibernateprojectdemo.aop;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.bfs.hibernateprojectdemo.dto.DataResponse;
import com.bfs.hibernateprojectdemo.exception.InvalidTokenException;
import com.bfs.hibernateprojectdemo.exception.ResourceNotFoundException;
import com.bfs.hibernateprojectdemo.exception.ValidationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<DataResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        DataResponse response = DataResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<DataResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        DataResponse response = DataResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<DataResponse> handleInvalidTokenException(InvalidTokenException ex) {
        DataResponse response = DataResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<DataResponse> handleValidationException(ValidationException ex) {
        BindingResult result = ex.getBindingResult();

        // Extract field errors and format them
        List<String> errors = result.getFieldErrors().stream()
                .map(fieldError -> String.format("%s: %s", fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());

        DataResponse response = DataResponse.builder()
                .success(false)
                .message("Validation failed")
                .data(errors) // Include formatted validation errors
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<DataResponse> handleAccessDeniedException(AccessDeniedException ex) {
        DataResponse response = DataResponse.builder()
                .success(false)
                .message("Access denied: " + ex.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN); 
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<DataResponse> handleGenericException(Exception ex) {
        DataResponse response = DataResponse.builder()
                .success(false)
                .message("An unexpected error occurred: " + ex.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}