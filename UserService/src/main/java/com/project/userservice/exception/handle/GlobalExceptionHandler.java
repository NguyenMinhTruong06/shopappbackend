package com.project.userservice.exception.handle;

import com.project.userservice.dto.error.ErrorResponseDto;
import com.project.userservice.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(ValidationException ex) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .status(ex.getStatus())
                .detail(ex.getMessage())
                .build();
        return ResponseEntity.badRequest().body(errorResponseDto);
    }
}
