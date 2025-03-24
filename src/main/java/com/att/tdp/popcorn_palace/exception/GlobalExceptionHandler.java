package com.att.tdp.popcorn_palace.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

// Global exception handler for all controllers
@RestControllerAdvice
public class GlobalExceptionHandler {

     /**
     * Handles validation errors thrown when @Valid fails in request DTOs.
     * Returns a map of field names and corresponding error messages.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage()));
        return errors;
    }

    /**
     * Handles custom "not found" exceptions (e.g., when an entity with a given ID is missing).
     * Returns a 404 NOT FOUND status and an error message.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFound(ResourceNotFoundException ex) {
        return Map.of("error", ex.getMessage());
    }

    /**
     * Handles Spring's ResponseStatusException, which allows throwing errors with specific HTTP status codes.
     * Returns a response with the exact status and reason message.
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(Map.of("error", ex.getReason()));
    }

     /**
     * Handles any other unexpected exceptions that weren't specifically handled above.
     * Returns a 500 INTERNAL SERVER ERROR with a generic message.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleGenericException(Exception ex) {
        return Map.of("error", "Unexpected error occurred");
    }
}
