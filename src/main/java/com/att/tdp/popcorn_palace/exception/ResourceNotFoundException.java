package com.att.tdp.popcorn_palace.exception;

/**
 * Custom exception thrown when a requested resource (e.g., movie, booking, showtime)
 * is not found in the system.
 * This exception is handled globally by GlobalExceptionHandler to return a 404 response.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    } 
}
