package com.project.userservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

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
