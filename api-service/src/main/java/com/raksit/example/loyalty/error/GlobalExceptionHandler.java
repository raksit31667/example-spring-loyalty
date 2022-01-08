package com.raksit.example.loyalty.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolationException(
      ConstraintViolationException ex) {
    return ResponseEntity.badRequest()
        .body(ErrorResponse.builder()
            .code(ErrorCode.INVALID_USER_ID)
            .message(ex.getConstraintViolations().iterator().next().getMessage())
            .build()
        );
  }
}
