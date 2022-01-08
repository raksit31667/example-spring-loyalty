package com.raksit.example.loyalty.error;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Collections;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

  @Mock
  private ConstraintViolation<?> constraintViolation;

  @InjectMocks
  private GlobalExceptionHandler exceptionHandler;

  @Test
  void shouldReturnErrorResponseWithBadRequest_whenHandleConstraintViolationException() {
    // Given
    final ConstraintViolationException exception = new ConstraintViolationException(
        Collections.singleton(constraintViolation));
    when(constraintViolation.getMessage()).thenReturn("invalid user id");

    // When
    final ResponseEntity<ErrorResponse> actual = exceptionHandler
        .handleConstraintViolationException(exception);

    // Then
    assertThat(actual.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
    assertThat(actual.getBody(), equalTo(ErrorResponse.builder()
        .code(ErrorCode.INVALID_USER_ID)
        .message("invalid user id")
        .build()));
  }
}