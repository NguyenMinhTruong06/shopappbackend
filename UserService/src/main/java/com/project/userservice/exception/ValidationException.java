package com.project.userservice.exception;


import org.springframework.http.HttpStatus;


public class ValidationException extends RuntimeException {
    private HttpStatus status;
    public ValidationException() {

    }

    public ValidationException(HttpStatus status, String message) {
        super(message);
        this.status=status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
