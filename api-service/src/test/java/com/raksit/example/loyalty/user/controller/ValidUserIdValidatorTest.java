package com.raksit.example.loyalty.user.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ValidUserIdValidatorTest {

  @Mock
  private ValidUserId validUserId;

  private ValidUserIdValidator validator;

  @BeforeEach
  void setUp() {
    validator = new ValidUserIdValidator();
    validator.initialize(validUserId);
  }

  @Test
  void shouldReturnTrue_whenValidate_givenUUIDFromValidString() {
    // When
    // Then
    assertThat(validator.validate("bbad724a-5645-4af3-b17d-0f5ee006ee50"), equalTo(true));
  }

  @Test
  void shouldReturnFalse_whenValidate_givenUUIDFromInvalidString() {
    // When
    // Then
    assertThat(validator.validate("abcd"), equalTo(false));
  }
}