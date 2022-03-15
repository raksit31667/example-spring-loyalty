package com.raksit.example.loyalty.feature;

import static com.raksit.example.loyalty.feature.FeatureName.EXAMPLE_SPRING_LOYALTY_EXAMPLE_FEATURE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

import io.split.client.SplitClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SplitFeatureToggleServiceTest {

  @Mock
  private SplitClient splitClient;

  @InjectMocks
  private SplitFeatureToggleService splitFeatureToggleService;

  @Test
  void shouldReturnTrue_whenIsEnabled_givenSplitTreatmentOn() {
    // Given
    when(splitClient.getTreatment("", EXAMPLE_SPRING_LOYALTY_EXAMPLE_FEATURE.name()))
        .thenReturn("on");

    // When
    // Then
    assertThat(splitFeatureToggleService.isEnabled(EXAMPLE_SPRING_LOYALTY_EXAMPLE_FEATURE), equalTo(true));
  }

  @Test
  void shouldReturnFalse_whenIsEnabled_givenSplitTreatmentOff() {
    // Given
    when(splitClient.getTreatment("", EXAMPLE_SPRING_LOYALTY_EXAMPLE_FEATURE.name()))
        .thenReturn("off");

    // When
    // Then
    assertThat(splitFeatureToggleService.isEnabled(EXAMPLE_SPRING_LOYALTY_EXAMPLE_FEATURE), equalTo(false));
  }

  @Test
  void shouldReturnFalse_whenIsEnabled_givenSplitHasError() {
    // Given
    when(splitClient.getTreatment("", EXAMPLE_SPRING_LOYALTY_EXAMPLE_FEATURE.name()))
        .thenReturn("control");

    // When
    // Then
    assertThat(splitFeatureToggleService.isEnabled(EXAMPLE_SPRING_LOYALTY_EXAMPLE_FEATURE), equalTo(false));
  }
}