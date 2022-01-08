package com.raksit.example.loyalty.validator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BaseValidatorTest {

  @Mock
  private ConstraintValidatorContext validatorContext;

  @Mock
  private ConstraintViolationBuilder violationBuilder;

  @Spy
  private FakeBaseValidator validator;

  @Test
  void shouldReturnTrue_whenIsValid_givenValidateValueReturnsTrue() {
    // Given
    doReturn(true).when(validator).validate("fakeValue");

    // When
    // Then
    assertThat(validator.isValid("fakeValue", validatorContext), equalTo(true));
  }

  @Test
  void shouldReturnFalseAndSetContextMessage_whenIsValid_givenValidateValueReturnsFalse() {
    // Given
    validator.setMessage("fakeMessage");
    doReturn(false).when(validator).validate("fakeValue");
    when(validatorContext.buildConstraintViolationWithTemplate("fakeMessage"))
        .thenReturn(violationBuilder);
    when(violationBuilder.addConstraintViolation()).thenReturn(validatorContext);

    // When
    // Then
    assertThat(validator.isValid("fakeValue", validatorContext), equalTo(false));
    verify(validatorContext).disableDefaultConstraintViolation();
  }

  private @interface FakeAnnotation {

    String message() default "fakeMessage";
  }

  private static class FakeBaseValidator extends BaseValidator<FakeAnnotation, String> {

    @Override
    public boolean validate(String value) {
      return false;
    }

    @Override
    public void initialize(FakeAnnotation constraintAnnotation) {
      this.setMessage(constraintAnnotation.message());
    }
  }
}