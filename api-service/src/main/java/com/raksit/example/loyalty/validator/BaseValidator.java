package com.raksit.example.loyalty.validator;

import lombok.Setter;

import java.lang.annotation.Annotation;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Setter
public abstract class BaseValidator<A extends Annotation, T> implements ConstraintValidator<A, T> {

  private String message;

  @Override
  public boolean isValid(T value, ConstraintValidatorContext context) {
    if (validate(value)) {
      return true;
    } else {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(message)
          .addConstraintViolation();
      return false;
    }
  }

  public abstract boolean validate(T value);
}
