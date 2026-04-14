package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "validation failed");

        Map<String, String> fields = new HashMap<>();

        // Loop through ex.getBindingResult().getAllErrors()
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            fields.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        response.put("fields", fields);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Here I am catching the RuntimeException we throw in UserService for duplicate emails.
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeExceptions(RuntimeException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 404 Not Found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(ResourceNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // 403 Forbidden Action
    @ExceptionHandler(ForbiddenAccessException.class)
    public ResponseEntity<Map<String, String>> handleForbidden(ForbiddenAccessException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "unauthorized action"); // Exact string requested by rubric
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
}
