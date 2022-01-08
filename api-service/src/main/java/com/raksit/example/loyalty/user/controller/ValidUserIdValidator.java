package com.raksit.example.loyalty.user.controller;

import com.raksit.example.loyalty.validator.BaseValidator;

import java.util.UUID;

public class ValidUserIdValidator extends BaseValidator<ValidUserId, String> {

  @Override
  public boolean validate(String value) {
    try {
      UUID.fromString(value);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  @Override
  public void initialize(ValidUserId constraintAnnotation) {
    this.setMessage(constraintAnnotation.message());
  }
}